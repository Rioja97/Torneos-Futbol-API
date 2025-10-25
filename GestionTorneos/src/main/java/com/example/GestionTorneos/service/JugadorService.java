package com.example.GestionTorneos.service;

import com.example.GestionTorneos.DTO.jugador.JugadorCreateDTO;
import com.example.GestionTorneos.DTO.jugador.JugadorMapper;
import com.example.GestionTorneos.DTO.jugador.JugadorResponseDTO;
import com.example.GestionTorneos.DTO.jugador.JugadorUpdateDTO;
import com.example.GestionTorneos.excepcion.EntidadNoEncontradaException;
import com.example.GestionTorneos.excepcion.EntidadRepetidaException;
import com.example.GestionTorneos.model.Equipo;
import com.example.GestionTorneos.model.Jugador;
import com.example.GestionTorneos.repository.EquipoRepository;
import com.example.GestionTorneos.repository.JugadorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JugadorService {

    @Autowired
    private JugadorRepository jugadorRepository;
    @Autowired
    private EquipoRepository equipoRepository;
    @Autowired
    private JugadorMapper jugadorMapper;

    public List<JugadorResponseDTO> listarTodos() {
        return jugadorRepository.findAll()
                .stream()
                .map(jugadorMapper::jugadorToJugadorResponseDTO)
                .toList();
    }

    public JugadorResponseDTO buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un valor positivo.");
        }
        Jugador jugador = jugadorRepository.findById(id).orElseThrow(() -> new EntidadNoEncontradaException("El jugador con ID " + id + " no existe"));

        return jugadorMapper.jugadorToJugadorResponseDTO(jugador);
    }

    public JugadorResponseDTO crear(@Valid JugadorCreateDTO dto) {

        Equipo equipo = equipoRepository.findById(dto.equipoId())
                        .orElseThrow(() -> new EntidadNoEncontradaException("El equipo con ID " + dto.equipoId() + " no existe"));

        Jugador nuevojugador = jugadorMapper.crearDTOToJugador(dto);

        nuevojugador.setEquipo(equipo);
        validarLogicaNegocioCreacion(nuevojugador);
        Jugador jugador = jugadorRepository.save(nuevojugador);
        return  jugadorMapper.jugadorToJugadorResponseDTO(jugador);
    }

    public JugadorResponseDTO actualizar(Long id, @Valid JugadorUpdateDTO dto) {
        Jugador existente = jugadorRepository.findById(id)
                        .orElseThrow(() -> new EntidadNoEncontradaException("El jugador con ID " + id + " no existe"));
        jugadorMapper.actualizarJugadorDesdeDTO(dto, existente);

        if(dto.equipoId() != null){
            Equipo equipo = equipoRepository.findById(dto.equipoId())
                    .orElseThrow(() -> new EntidadNoEncontradaException("El equipo con ID " + dto.equipoId() + " no existe"));
            existente.setEquipo(equipo);
        }
        validarLogicaNegocioActualizacion(existente);
        Jugador jugadorActualizado = jugadorRepository.save(existente);


        return jugadorMapper.jugadorToJugadorResponseDTO(jugadorActualizado);
    }

    public void eliminar(Long id) {
        if(id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un valor positivo.");
        }
        jugadorRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("El jugador con ID " + id + " no existe"));

        jugadorRepository.deleteById(id);
    }


    private void validarLogicaNegocioCreacion(Jugador jugador) {
        if (jugador.getEdad() < 15) {
            throw new IllegalArgumentException("El jugador debe tener al menos 15 años.");
        }

        if (jugadorRepository.existsByEquipoIdAndDorsal(
                jugador.getEquipo().getId(),
                jugador.getDorsal()
        )) {
            throw new EntidadRepetidaException("Ya existe un jugador con ese dorsal en el equipo.");
        }
    }

    private void validarLogicaNegocioActualizacion(Jugador actualizado) {
        if (actualizado.getEdad() < 15) {
            throw new IllegalArgumentException("El jugador debe tener al menos 15 años.");
        }

        boolean dorsalOcupado = jugadorRepository.existsByEquipoIdAndDorsalAndIdNot(
                actualizado.getEquipo().getId(),
                actualizado.getDorsal(),
                actualizado.getId()
        );

        if (dorsalOcupado) {
            throw new EntidadRepetidaException("Ya existe otro jugador con ese dorsal en el equipo.");
        }
    }
}
