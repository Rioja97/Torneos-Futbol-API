package com.example.GestionTorneos.service;

// ... (todas tus importaciones DTO) ...
import com.example.GestionTorneos.DTO.partido.PartidoResponseDTO; // <-- IMPORTANTE
import com.example.GestionTorneos.DTO.partido.PartidoMapper; // <-- IMPORTANTE
import com.example.GestionTorneos.DTO.partido.PartidoResponseDTO;
import com.example.GestionTorneos.DTO.torneo.TorneoCreateDTO;
import com.example.GestionTorneos.DTO.torneo.TorneoMapper;
import com.example.GestionTorneos.DTO.torneo.TorneoResponseDTO;
import com.example.GestionTorneos.excepcion.CupoMaximoException;
// ... (todas tus otras importaciones de excepciones) ...
import com.example.GestionTorneos.excepcion.EntidadNoEncontradaException;
import com.example.GestionTorneos.excepcion.NoNuloException;
import com.example.GestionTorneos.model.Equipo;
import com.example.GestionTorneos.model.Partido;
import com.example.GestionTorneos.model.Torneo;
import com.example.GestionTorneos.repository.EquipoRepository;
import com.example.GestionTorneos.repository.PartidoRepository; // <-- IMPORTANTE
import com.example.GestionTorneos.repository.TorneoRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final EquipoRepository equipoRepository;
    private final PartidoRepository partidoRepository; // <-- CAMBIO: Inyectamos el Repo
    private final TorneoMapper torneoMapper;
    private final PartidoMapper partidoMapper; // <-- CAMBIO: Inyectamos el Mapper

    // CAMBIO: Inyección por constructor (mejor práctica)
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
        // ... (tu lógica de validación de partidos jugados está bien) ...
        for (Partido p : aEliminar.getPartidos()) {
            if(p.getResultado() == null){
                throw new NoNuloException("No se pueden eliminar torneos con partidos por jugarse");
            }
        }
        torneoRepository.deleteById(id);
    }

    // ... (tus métodos de validación están bien) ...
    private void validarLogicaNegocioCreacion(Torneo torneo) { /* ... tu código ... */ }
    private void validarLogicaNegocioActualizacion(Torneo actualizado) { /* ... tu código ... */ }

    // --- MÉTODOS CORREGIDOS ---

    @Transactional
    public List<Partido> agregarPartidos(Long idTorneo, List<Long> idPartidos) {
        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        List<Partido> partidosAgregados = new ArrayList<>();

        for (Long idPartido : idPartidos) {
            // CAMBIO: Buscamos la ENTIDAD usando el REPO
            Partido partido = partidoRepository.findById(idPartido)
                    .orElseThrow(() -> new RuntimeException("Partido no encontrado"));

            // CAMBIO: Modificamos la ENTIDAD
            partido.setTorneo(torneo);

            // CAMBIO: Guardamos la ENTIDAD usando el REPO
            Partido partidoGuardado = partidoRepository.save(partido);
            partidosAgregados.add(partidoGuardado);
        }

        // Esto ya no es necesario si guardas dentro del bucle
        // torneo.getPartidos().addAll(partidosAgregados);
        // torneoRepository.save(torneo);

        return partidosAgregados;
    }


    @Transactional
    public void eliminarPartidoDeTorneo(Long idTorneo, Long idPartido) {
        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        // CAMBIO: Buscamos la ENTIDAD usando el REPO
        Partido partido = partidoRepository.findById(idPartido)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado"));

        if (!torneo.getPartidos().contains(partido)) {
            throw new RuntimeException("El partido no pertenece a este torneo");
        }

        torneo.getPartidos().remove(partido); // Quita la relación
        partidoRepository.delete(partido); // CAMBIO: Borra el partido usando el REPO
    }

    @Transactional(readOnly = true)
    public List<PartidoResponseDTO> listarPartidosDeTorneo(Long idTorneo) {
        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        // CAMBIO: Devolvemos DTOs, no entidades
        return torneo.getPartidos()
                .stream()
                .map(partidoMapper::partidoToPartidoResponseDTO)
                .toList();
    }
}