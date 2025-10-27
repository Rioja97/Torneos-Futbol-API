package com.example.GestionTorneos.service;

import com.example.GestionTorneos.DTO.entrenador.EntrenadorCreateDTO;
import com.example.GestionTorneos.DTO.entrenador.EntrenadorMapper;
import com.example.GestionTorneos.DTO.entrenador.EntrenadorResponseDTO;
import com.example.GestionTorneos.DTO.entrenador.EntrenadorUpdateDTO;
import com.example.GestionTorneos.DTO.jugador.JugadorCreateDTO;
import com.example.GestionTorneos.DTO.jugador.JugadorResponseDTO;
import com.example.GestionTorneos.DTO.jugador.JugadorUpdateDTO;
import com.example.GestionTorneos.excepcion.EntidadNoEncontradaException;
import com.example.GestionTorneos.excepcion.ValorPositivoException;
import com.example.GestionTorneos.model.Entrenador;
import com.example.GestionTorneos.model.Equipo;
import com.example.GestionTorneos.model.Jugador;
import com.example.GestionTorneos.repository.EntrenadorRepository;
import com.example.GestionTorneos.repository.EquipoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntrenadorService {

    @Autowired
    private EntrenadorRepository entrenadorRepository;
    @Autowired
    private EquipoRepository equipoRepository;
    @Autowired
    EntrenadorMapper entrenadorMapper;

    public List<EntrenadorResponseDTO> listarTodos() {
        return entrenadorRepository.findAll()
                .stream()
                .map(entrenadorMapper::entrenadorToEntrenadorResponseDTO)
                .toList();
    }

    public EntrenadorResponseDTO buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un valor positivo.");
        }
        Entrenador entrenador = entrenadorRepository.findById(id).orElseThrow(() -> new EntidadNoEncontradaException("El entrenador con ID " + id + " no existe"));

        return entrenadorMapper.entrenadorToEntrenadorResponseDTO(entrenador);
    }

    @Transactional
    public EntrenadorResponseDTO crear(@Valid EntrenadorCreateDTO dto) {

        Equipo equipo = equipoRepository.findById(dto.equipoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("El equipo con ID " + dto.equipoId() + " no existe"));

        Entrenador nuevoEntrenador = entrenadorMapper.crearDTOToEntrenador(dto);

        nuevoEntrenador.setEquipo(equipo);
        Entrenador entrenador = entrenadorRepository.save(nuevoEntrenador);
        return  entrenadorMapper.entrenadorToEntrenadorResponseDTO(entrenador);
    }

    public EntrenadorResponseDTO actualizar(Long id, @Valid EntrenadorUpdateDTO dto) {
        Entrenador existente = entrenadorRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("El entrenador con ID " + id + " no existe"));
        entrenadorMapper.actualizarEntrenadorDesdeDTO(dto, existente);

        if(dto.equipoId() != null){
            Equipo equipo = equipoRepository.findById(dto.equipoId())
                    .orElseThrow(() -> new EntidadNoEncontradaException("El equipo con ID " + dto.equipoId() + " no existe"));
            existente.setEquipo(equipo);
        }
        validarEntrenador(existente);
        Entrenador entrenadorActualizado = entrenadorRepository.save(existente);

        return entrenadorMapper.entrenadorToEntrenadorResponseDTO(entrenadorActualizado);
    }


    public void eliminar(Long id) {
        if(id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un valor positivo.");
        }
        entrenadorRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("El jugador con ID " + id + " no existe"));

        entrenadorRepository.deleteById(id);
    }

    private void validarEntrenador(Entrenador entrenador) {
        if (entrenador.getEquipo() != null) {
            Long entrenadorId = entrenador.getId() != null ? entrenador.getId() : -1L;
            boolean entrenadorAsignado = entrenadorRepository.existsByEquipoIdAndIdNot(
                    entrenador.getEquipo().getId(), entrenadorId
            );
            if (entrenadorAsignado) {
                throw new IllegalArgumentException("El entrenador ya está asignado a otro equipo.");
            }
        }
    }
}