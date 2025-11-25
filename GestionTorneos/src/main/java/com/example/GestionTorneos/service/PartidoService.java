package com.example.GestionTorneos.service;

import com.example.GestionTorneos.DTO.estadistica.EstadisticaJugadorDTO;
import com.example.GestionTorneos.DTO.estadistica.ResultadoPartidoDTO;
import com.example.GestionTorneos.DTO.partido.*;
import com.example.GestionTorneos.excepcion.EntidadNoEncontradaException;
import com.example.GestionTorneos.model.*;
import com.example.GestionTorneos.repository.EquipoRepository;
import com.example.GestionTorneos.repository.JugadorRepository;
import com.example.GestionTorneos.repository.PartidoRepository;
import com.example.GestionTorneos.repository.TorneoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class PartidoService {

    private final PartidoRepository partidoRepository;
    private final JugadorRepository jugadorRepository;
    private final EquipoRepository equipoRepository;
    private final TorneoRepository torneoRepository;
    private final PartidoMapper partidoMapper;

    public PartidoService(PartidoRepository partidoRepository,
                          JugadorRepository jugadorRepository,
                          EquipoRepository equipoRepository,
                          TorneoRepository torneoRepository,
                          PartidoMapper partidoMapper) {
        this.partidoRepository = partidoRepository;
        this.jugadorRepository = jugadorRepository;
        this.equipoRepository = equipoRepository;
        this.torneoRepository = torneoRepository;
        this.partidoMapper = partidoMapper;
    }

    public PartidoDetailDTO buscarPorId(Long id) {
        Partido partido = partidoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Partido no encontrado con id: " + id));
        return partidoMapper.partidoToPartidoDetailDTO(partido);
    }

    @Transactional
    public PartidoDetailDTO crearPartidoEnTorneo(Long idTorneo, PartidoCreateDTO dto) {

        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new EntidadNoEncontradaException("Torneo no encontrado"));

        Equipo local = equipoRepository.findById(dto.equipoLocalId())
                .orElseThrow(() -> new EntidadNoEncontradaException("Equipo local no encontrado"));
        Equipo visitante = equipoRepository.findById(dto.equipoVisitanteId())
                .orElseThrow(() -> new EntidadNoEncontradaException("Equipo visitante no encontrado"));

        Partido nuevoPartido = new Partido();

        nuevoPartido.setTorneo(torneo);
        nuevoPartido.setEquipoLocal(local);
        nuevoPartido.setEquipoVisitante(visitante);
        nuevoPartido.setFecha(dto.fecha());
        nuevoPartido.setJugado(false);
        nuevoPartido.setEstadisticas(new ArrayList<>());

        validarLogicaNegocioCreacion(nuevoPartido);

        Partido partidoGuardado = partidoRepository.save(nuevoPartido);

        return partidoMapper.partidoToPartidoDetailDTO(partidoGuardado);
    }

    public void eliminarPartidoDeTorneo(Long idTorneo, Long idPartido) {
        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new EntidadNoEncontradaException("Torneo no encontrado"));

        Partido partido = partidoRepository.findById(idPartido)
                .orElseThrow(() -> new EntidadNoEncontradaException("Partido no encontrado"));

        partidoRepository.delete(partido);
    }

    @Transactional
    public PartidoDetailDTO actualizar(Long id, PartidoUpdateDTO dto) {

        Partido partidoExistente = partidoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Partido no encontrado"));

        if (dto.equipoLocalId() != null) {
            Equipo local = equipoRepository.findById(dto.equipoLocalId())
                    .orElseThrow(() -> new EntidadNoEncontradaException("Equipo local no encontrado"));
            partidoExistente.setEquipoLocal(local);
        }

        if (dto.equipoVisitanteId() != null) {
            Equipo visitante = equipoRepository.findById(dto.equipoVisitanteId())
                    .orElseThrow(() -> new EntidadNoEncontradaException("Equipo visitante no encontrado"));
            partidoExistente.setEquipoVisitante(visitante);
        }

        if (dto.torneoId() != null) {
            Torneo torneo = torneoRepository.findById(dto.torneoId())
                    .orElseThrow(() -> new EntidadNoEncontradaException("Torneo no encontrado"));
            partidoExistente.setTorneo(torneo);
        }

        if (dto.fecha() != null) {
            partidoExistente.setFecha(dto.fecha());
        }

        validarLogicaNegocioActualizacion(partidoExistente);
        Partido partidoActualizado = partidoRepository.save(partidoExistente);

        return partidoMapper.partidoToPartidoDetailDTO(partidoActualizado);
    }


    @Transactional
    public void registrarResultadoYEstadisticas(Long partidoId, ResultadoPartidoDTO dto) {
        Partido partido = partidoRepository.findById(partidoId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Partido no encontrado"));

        Set<Long> idsEquiposParticipantes = Set.of(
                partido.getEquipoLocal().getId(),
                partido.getEquipoVisitante().getId()
        );

        partido.getEstadisticas().clear();
        List<Estadistica> estadisticas = new ArrayList<>();

        for (EstadisticaJugadorDTO estDto : dto.estadisticasJugadores()) {

            Jugador jugador = jugadorRepository.findById(estDto.jugadorId())
                    .orElseThrow(() -> new EntityNotFoundException("Jugador no encontrado con ID: " + estDto.jugadorId()));

            if (jugador.getEquipo() == null || !idsEquiposParticipantes.contains(jugador.getEquipo().getId())) {
                throw new IllegalArgumentException(
                        "El jugador " + jugador.getNombre() + " (ID: " + jugador.getId() + ") " +
                                "no pertenece a ninguno de los equipos de este partido."
                );
            }

            Estadistica estadistica = new Estadistica();
            estadistica.setPartido(partido);
            estadistica.setJugador(jugador);
            estadistica.setTorneo(partido.getTorneo());  // ← AGREGAR ESTA LÍNEA
            estadistica.setGoles(estDto.goles());
            estadistica.setAsistencias(estDto.asistencias());
            estadistica.setTarjetasAmarillas(estDto.tarjetasAmarillas());
            estadistica.setTarjetasRojas(estDto.tarjetasRojas());

            estadisticas.add(estadistica);
        }

        partido.setResultado(dto.resultado());
        partido.setJugado(true);
        partido.getEstadisticas().addAll(estadisticas);

        partidoRepository.save(partido);
    }

    private void validarLogicaNegocioCreacion(Partido partido) {

        if (partido.getEquipoLocal().getId().equals(partido.getEquipoVisitante().getId())) {
            throw new IllegalArgumentException("El equipo local y visitante no pueden ser el mismo.");
        }

        if (partido.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede programar un partido en una fecha pasada.");
        }

        Long localId = partido.getEquipoLocal().getId();
        Long visitanteId = partido.getEquipoVisitante().getId();
        LocalDate fecha = partido.getFecha();

        boolean localYaJuega = partidoRepository.existsByFechaAndEquipoLocalId(fecha, localId)
                || partidoRepository.existsByFechaAndEquipoVisitanteId(fecha, localId);

        boolean visitanteYaJuega = partidoRepository.existsByFechaAndEquipoLocalId(fecha, visitanteId)
                || partidoRepository.existsByFechaAndEquipoVisitanteId(fecha, visitanteId);

        if (localYaJuega || visitanteYaJuega) {
            throw new IllegalArgumentException("Uno de los equipos ya tiene un partido en esa fecha.");
        }
    }


    private void validarLogicaNegocioActualizacion(Partido partidoActualizado) {
        if (partidoActualizado.getEquipoLocal().getId().equals(partidoActualizado.getEquipoVisitante().getId())) {
            throw new IllegalArgumentException("El equipo local y visitante no pueden ser el mismo.");
        }

        boolean yaJuegaEseDia = partidoRepository.existsByFechaAndEquiposExcluyendoId(
                partidoActualizado.getFecha(),
                partidoActualizado.getEquipoLocal().getId(),
                partidoActualizado.getEquipoVisitante().getId(),
                partidoActualizado.getId()
        );
        if (yaJuegaEseDia) {
            throw new IllegalArgumentException("Uno de los equipos ya tiene un partido en esa fecha.");
        }
    }
}