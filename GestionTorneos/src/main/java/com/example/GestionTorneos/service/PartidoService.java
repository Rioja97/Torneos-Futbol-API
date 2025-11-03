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
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class PartidoService {

    // Dependencias inyectadas por constructor
    private final PartidoRepository partidoRepository;
    private final JugadorRepository jugadorRepository;
    private final EquipoRepository equipoRepository;
    private final TorneoRepository torneoRepository;
    private final PartidoMapper partidoMapper;

    // Inyección por constructor (la mejor práctica)
    public PartidoService(PartidoRepository partidoRepository,
                          JugadorRepository jugadorRepository,
                          EquipoRepository equipoRepository,
                          TorneoRepository torneoRepository,
                          PartidoMapper partidoMapper) {
        this.partidoRepository = partidoRepository;
        this.jugadorRepository = jugadorRepository; // <-- ESTO SOLUCIONA TU ERROR
        this.equipoRepository = equipoRepository;
        this.torneoRepository = torneoRepository;
        this.partidoMapper = partidoMapper;
    }

    public List<PartidoResponseDTO> listarTodos() {
        return partidoRepository.findAll()
                .stream()
                .map(partidoMapper::partidoToPartidoResponseDTO) // Usamos el Mapper
                .toList();
    }

    public PartidoDetailDTO buscarPorId(Long id) {
        Partido partido = partidoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Partido no encontrado con id: " + id));
        return partidoMapper.partidoToPartidoDetailDTO(partido); // Usamos el Mapper
    }

    @Transactional
    public PartidoDetailDTO crear(@Valid PartidoCreateDTO dto) {
        // 1. Buscamos las entidades reales usando los IDs del DTO
        Equipo local = equipoRepository.findById(dto.equipoLocalId())
                .orElseThrow(() -> new EntidadNoEncontradaException("Equipo local no encontrado"));
        Equipo visitante = equipoRepository.findById(dto.equipoVisitanteId())
                .orElseThrow(() -> new EntidadNoEncontradaException("Equipo visitante no encontrado"));
        Torneo torneo = torneoRepository.findById(dto.torneoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("Torneo no encontrado"));

        // 2. Creamos la nueva entidad Partido
        Partido nuevoPartido = new Partido();
        nuevoPartido.setEquipoLocal(local);
        nuevoPartido.setEquipoVisitante(visitante);
        nuevoPartido.setTorneo(torneo);
        nuevoPartido.setFecha(dto.fecha());

        // 3. Seteamos valores por defecto
        nuevoPartido.setJugado(false);
        nuevoPartido.setEstadisticas(new ArrayList<>()); // ¡Importante inicializar!

        // 4. Validamos la lógica de negocio (tu método privado)
        validarLogicaNegocioCreacion(nuevoPartido);

        // 5. Guardamos y mapeamos la respuesta
        Partido partidoGuardado = partidoRepository.save(nuevoPartido);
        return partidoMapper.partidoToPartidoDetailDTO(partidoGuardado);
    }

    @Transactional
    public PartidoDetailDTO actualizar(Long id, PartidoUpdateDTO dto) {
        // 1. Buscamos el partido existente
        Partido partidoExistente = partidoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Partido no encontrado"));

        // 2. Revisamos campo por campo
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

        // 3. Validamos la lógica de negocio con los nuevos datos
        validarLogicaNegocioActualizacion(partidoExistente);

        // 4. Guardamos y mapeamos la respuesta
        Partido partidoActualizado = partidoRepository.save(partidoExistente);
        return partidoMapper.partidoToPartidoDetailDTO(partidoActualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        Partido existente = partidoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Partido no encontrado"));
        partidoRepository.delete(existente);
    }

    // --- LÓGICA DE REGISTRAR RESULTADO (TU CÓDIGO CASI PERFECTO) ---
    // (Ahora funciona gracias a la inyección por constructor de JugadorRepository)
    // ... (importaciones)


// ... (en la clase PartidoService)

    @Transactional
    public void registrarResultadoYEstadisticas(Long partidoId, ResultadoPartidoDTO dto) {
        // 1. Busca el partido (esto ya lo tenías)
        Partido partido = partidoRepository.findById(partidoId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Partido no encontrado"));

        if (partido.isJugado()) {
            throw new IllegalStateException("El partido ya fue jugado.");
        }

        // 2. OBTENEMOS LOS EQUIPOS QUE JUEGAN
        // Para ser más eficientes, creamos un Set con los IDs de los equipos válidos
        Set<Long> idsEquiposParticipantes = Set.of(
                partido.getEquipoLocal().getId(),
                partido.getEquipoVisitante().getId()
        );

        partido.getEstadisticas().clear();
        List<Estadistica> estadisticas = new ArrayList<>();

        for (EstadisticaJugadorDTO estDto : dto.estadisticasJugadores()) {

            // 3. Buscamos al jugador
            Jugador jugador = jugadorRepository.findById(estDto.jugadorId())
                    .orElseThrow(() -> new EntityNotFoundException("Jugador no encontrado con ID: " + estDto.jugadorId()));

            // 4. ¡AQUÍ ESTÁ LA VALIDACIÓN!
            if (jugador.getEquipo() == null || !idsEquiposParticipantes.contains(jugador.getEquipo().getId())) {
                // Si el jugador no tiene equipo, o si el ID de su equipo
                // no está en nuestro Set de equipos válidos, lanzamos un error.
                throw new IllegalArgumentException(
                        "El jugador " + jugador.getNombre() + " (ID: " + jugador.getId() + ") " +
                                "no pertenece a ninguno de los equipos de este partido."
                );
            }

            // 5. Si la validación pasa, creamos la estadística (como antes)
            Estadistica estadistica = new Estadistica();
            estadistica.setPartido(partido);
            estadistica.setJugador(jugador);
            estadistica.setGoles(estDto.goles());
            estadistica.setAsistencias(estDto.asistencias());
            estadistica.setTarjetasAmarillas(estDto.tarjetasAmarillas());
            estadistica.setTarjetasRojas(estDto.tarjetasRojas());

            estadisticas.add(estadistica);
        }

        // 6. Guardamos todo (como antes)
        partido.setResultado(dto.resultado());
        partido.setJugado(true);
        partido.getEstadisticas().addAll(estadisticas);

        partidoRepository.save(partido);
    }


    // --- TUS MÉTODOS PRIVADOS DE VALIDACIÓN (MODIFICADOS LIGERAMENTE) ---
    // (Estos métodos ya existían en tu service, los adaptamos)

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

    private void validarLogicaNegocioActualizacion(Partido partidoActualizado) {
        if (partidoActualizado.getEquipoLocal().getId().equals(partidoActualizado.getEquipoVisitante().getId())) {
            throw new IllegalArgumentException("El equipo local y visitante no pueden ser el mismo.");
        }

        // Asumo que tu repo tiene este método (basado en tu código anterior)
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