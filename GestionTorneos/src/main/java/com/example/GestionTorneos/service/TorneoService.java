package com.example.GestionTorneos.service;

import com.example.GestionTorneos.DTO.partido.PartidoResponseDTO;
import com.example.GestionTorneos.DTO.partido.PartidoMapper;
import com.example.GestionTorneos.DTO.torneo.TorneoCreateDTO;
import com.example.GestionTorneos.DTO.torneo.TorneoMapper;
import com.example.GestionTorneos.DTO.torneo.TorneoResponseDTO;
import com.example.GestionTorneos.excepcion.CupoMaximoException;
import com.example.GestionTorneos.excepcion.EntidadNoEncontradaException;
import com.example.GestionTorneos.excepcion.EntidadRepetidaException;
import com.example.GestionTorneos.excepcion.NoNuloException;
import com.example.GestionTorneos.model.Equipo;
import com.example.GestionTorneos.model.Partido;
import com.example.GestionTorneos.model.Torneo;
import com.example.GestionTorneos.repository.EquipoRepository;
import com.example.GestionTorneos.repository.PartidoRepository;
import com.example.GestionTorneos.repository.TorneoRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final EquipoRepository equipoRepository;
    private final PartidoRepository partidoRepository;
    private final TorneoMapper torneoMapper;
    private final PartidoMapper partidoMapper;

    public TorneoService(TorneoRepository torneoRepository,
                         EquipoRepository equipoRepository,
                         PartidoRepository partidoRepository,
                         TorneoMapper torneoMapper,
                         PartidoMapper partidoMapper) {
        this.torneoRepository = torneoRepository;
        this.equipoRepository = equipoRepository;
        this.partidoRepository = partidoRepository;
        this.torneoMapper = torneoMapper;
        this.partidoMapper = partidoMapper;
    }

    public List<TorneoResponseDTO> listarTodos() {
        return torneoRepository.findAll()
                .stream()
                .map(torneoMapper::torneoToTorneoResponseDTO)
                .toList();
    }

    public TorneoResponseDTO buscarPorId(Long id) {
        Torneo torneo = torneoRepository.findById(id).orElseThrow(() -> new EntidadNoEncontradaException("El torneo con ID " + id + " no existe"));
        return torneoMapper.torneoToTorneoResponseDTO(torneo);
    }

    public TorneoResponseDTO crear(@Valid TorneoCreateDTO dto) {
        Torneo torneo = torneoMapper.torneoDTOToTorneo(dto);
        validarLogicaNegocioCreacion(torneo);
        torneo = torneoRepository.save(torneo);
        return torneoMapper.torneoToTorneoResponseDTO(torneo);
    }


    public TorneoResponseDTO actualizar(Long id, @Valid TorneoResponseDTO dto) {
        Torneo actualizado = torneoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("El torneo con ID " + id + " no existe"));
        torneoMapper.actualizarTorneoDesdeDTO(dto, actualizado);
        validarLogicaNegocioActualizacion(actualizado);
        torneoRepository.save(actualizado);
        return torneoMapper.torneoToTorneoResponseDTO(actualizado);
    }


    public void eliminar(Long id) {
        Torneo aEliminar = torneoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("El torneo con ID " + id + " no existe"));
        for (Partido p : aEliminar.getPartidos()) {
            if(p.getResultado() == null){
                throw new NoNuloException("No se pueden eliminar torneos con partidos por jugarse");
            }
        }
        torneoRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<PartidoResponseDTO> listarPartidosDeTorneo(Long idTorneo) {
        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        return torneo.getPartidos()
                .stream()
                .map(partidoMapper::partidoToPartidoResponseDTO)
                .toList();
    }


    private void validarLogicaNegocioCreacion(Torneo torneo) {

        if (torneo.getCupo() != null &&
                torneo.getEquiposParticipantes().size() > torneo.getCupo()) {
            throw new CupoMaximoException("El número de equipos excede el cupo permitido.");
        }

        if (torneoRepository.existsByNombreAndDivision(
                torneo.getNombre(), torneo.getDivision())) {
            throw new EntidadRepetidaException("Ya existe un torneo con ese nombre y división.");
        }

        Set<Long> idsUnicos = torneo.getEquiposParticipantes().stream()
                .map(Equipo::getId)
                .collect(Collectors.toSet());

        if (idsUnicos.size() != torneo.getEquiposParticipantes().size()) {
            throw new EntidadRepetidaException("No puede haber equipos duplicados en el torneo.");
        }
    }

    private void validarLogicaNegocioActualizacion(Torneo actualizado) {

        Integer nuevoCupo = actualizado.getCupo();

        Set<Long> idsUnicos = actualizado.getEquiposParticipantes().stream()
                .map(Equipo::getId)
                .collect(Collectors.toSet());

        if (idsUnicos.size() != actualizado.getEquiposParticipantes().size()) {
            throw new EntidadRepetidaException("No puede haber equipos duplicados en el torneo.");
        }

        Optional<Torneo> otroTorneo = torneoRepository.findByNombreAndDivision(
                actualizado.getNombre(), actualizado.getDivision());

        if (otroTorneo.isPresent() && !otroTorneo.get().getId().equals(actualizado.getId())) {
            throw new EntidadRepetidaException("Ya existe OTRO torneo con ese nombre y división.");
        }

        if (nuevoCupo != null) {
            Set<Long> equiposUsados = actualizado.getPartidos().stream()
                    .flatMap(p -> List.of(
                            p.getEquipoLocal().getId(),
                            p.getEquipoVisitante().getId()
                    ).stream())
                    .collect(Collectors.toSet());

            if (equiposUsados.size() > nuevoCupo) {
                throw new CupoMaximoException(
                        "No se puede establecer un cupo de " + nuevoCupo +
                                " equipos. Actualmente el torneo ya tiene " + equiposUsados.size() +
                                " equipos participando en partidos."
                );
            }
        }
    }

}
