package com.example.GestionTorneos.service;
import com.example.GestionTorneos.DTO.estadistica.EstadisticaJugadorDTO;
import com.example.GestionTorneos.DTO.estadistica.ResultadoPartidoDTO;
import com.example.GestionTorneos.excepcion.EntidadNoEncontradaException;
import com.example.GestionTorneos.model.Estadistica;
import com.example.GestionTorneos.model.Jugador;
import com.example.GestionTorneos.model.Partido;
import com.example.GestionTorneos.repository.JugadorRepository;
import com.example.GestionTorneos.repository.PartidoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PartidoService {

    @Autowired
    private PartidoRepository partidoRepository;
    private JugadorRepository jugadorRepository;

    public List<Partido> listarTodos() {
        return partidoRepository.findAll();
    }

    public Partido buscarPorId(Long id) {
        return partidoRepository.findByIdConTodo(id)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado con id: " + id));
    }


    @Transactional
    public Partido crear(Partido partido) {
        if (partido.getEstadisticas() != null) {
            for (Estadistica e : partido.getEstadisticas()) {
                e.setPartido(partido); // ← clave
            }
        }

        return partidoRepository.save(partido);
    }

    public Partido actualizar(Long id, Partido datosActualizados) {
        Partido existente = buscarPorId(id);
        validarLogicaNegocioActualizacion(datosActualizados, existente);

        existente.setEquipoLocal(datosActualizados.getEquipoLocal());
        existente.setEquipoVisitante(datosActualizados.getEquipoVisitante());
        existente.setFecha(datosActualizados.getFecha());
        existente.setResultado(datosActualizados.getResultado());

        return partidoRepository.save(existente);
    }

    public void eliminar(Long id) {
        Partido existente = buscarPorId(id);
        partidoRepository.delete(existente);
    }

    private void validarLogicaNegocioCreacion(Partido partido) {
        if (partido.getEquipoLocal().getId().equals(partido.getEquipoVisitante().getId())) {
            throw new IllegalArgumentException("El equipo local y visitante no pueden ser el mismo.");
        }

        if (partido.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede programar un partido en una fecha pasada.");
        }

        boolean yaJuegaEseDia = partidoRepository.existsByFechaAndEquipos(
                partido.getFecha(),
                partido.getEquipoLocal().getId(),
                partido.getEquipoVisitante().getId()
        );

        if (yaJuegaEseDia) {
            throw new IllegalArgumentException("Uno de los equipos ya tiene un partido en esa fecha.");
        }
    }


    private void validarLogicaNegocioActualizacion(Partido actualizado, Partido existente) {
        if (actualizado.getEquipoLocal().getId().equals(actualizado.getEquipoVisitante().getId())) {
            throw new IllegalArgumentException("El equipo local y visitante no pueden ser el mismo.");
        }


        boolean yaJuegaEseDia = partidoRepository.existsByFechaAndEquiposExcluyendoId(
                actualizado.getFecha(),
                actualizado.getEquipoLocal().getId(),
                actualizado.getEquipoVisitante().getId(),
                existente.getId()
        );

        if (yaJuegaEseDia) {
            throw new IllegalArgumentException("Uno de los equipos ya tiene un partido en esa fecha.");
        }
    }

    @Transactional
    public void registrarResultadoYEstadisticas(Long partidoId, ResultadoPartidoDTO dto) {
        Partido partido = partidoRepository.findById(partidoId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Partido no encontrado"));

        if (partido.isJugado()) {
            throw new IllegalStateException("El partido ya fue jugado.");
        }

        List<Estadistica> estadisticas = new ArrayList<>();

        for (EstadisticaJugadorDTO estDto : dto.getEstadisticasJugadores()) {
            Jugador jugador = jugadorRepository.findById(estDto.getJugadorId())
                    .orElseThrow(() -> new EntityNotFoundException("Jugador no encontrado"));

            Estadistica estadistica = new Estadistica();
            estadistica.setPartido(partido);
            estadistica.setJugador(jugador);
            estadistica.setGoles(estDto.getGoles());
            estadistica.setAsistencias(estDto.getAsistencias());
            estadistica.setTarjetasAmarillas(estDto.getTarjetasAmarillas());
            estadistica.setTarjetasRojas(estDto.getTarjetasRojas());

            estadisticas.add(estadistica);
        }

        partido.setResultado(dto.getResultado());
        partido.setJugado(true);
        partido.setEstadisticas(estadisticas);

        partidoRepository.save(partido);
    }

}

