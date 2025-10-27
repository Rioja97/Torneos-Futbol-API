package com.example.GestionTorneos.service;

import com.example.GestionTorneos.DTO.entrenador.EntrenadorCreateDTO;
import com.example.GestionTorneos.DTO.entrenador.EntrenadorResponseDTO;
import com.example.GestionTorneos.DTO.entrenador.EntrenadorUpdateDTO;
import com.example.GestionTorneos.DTO.equipo.EquipoCreateDTO;
import com.example.GestionTorneos.DTO.equipo.EquipoMapper;
import com.example.GestionTorneos.DTO.equipo.EquipoResponseDTO;
import com.example.GestionTorneos.DTO.equipo.EquipoUpdateDTO;
import com.example.GestionTorneos.DTO.estadio.EstadioMapper;
import com.example.GestionTorneos.excepcion.EntidadNoEncontradaException;
import com.example.GestionTorneos.model.Entrenador;
import com.example.GestionTorneos.model.Equipo;
import com.example.GestionTorneos.model.Estadio;
import com.example.GestionTorneos.repository.EquipoRepository;
import com.example.GestionTorneos.repository.JugadorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;
    @Autowired
    private JugadorRepository jugadorRepository;
    @Autowired
    private EquipoMapper equipoMapper;
    @Autowired
    EstadioMapper estadioMapper;

    public List<EquipoResponseDTO> listarTodos() {
        return equipoRepository.findAll()
                .stream()
                .map(equipoMapper::responseDTOToEquipo)
                .toList();
    }

    public EquipoResponseDTO buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un valor positivo.");
        }
        Equipo equipo = equipoRepository.findById(id).orElseThrow(() -> new EntidadNoEncontradaException("El equipo con ID " + id + " no existe"));

        return equipoMapper.responseDTOToEquipo(equipo);
    }

    public EquipoResponseDTO crear(@Valid EquipoCreateDTO dto) {

        Equipo equipo = equipoMapper.createDTOToEquipo(dto);

        validarEquipo(equipo);

        equipoRepository.save(equipo);
        return  equipoMapper.responseDTOToEquipo(equipo);
    }

    public EquipoResponseDTO actualizar(Long id, @Valid EquipoUpdateDTO dto) {

        Equipo equipo = null;

        if(id != null){
            equipo = equipoRepository.findById(id)
                    .orElseThrow(() -> new EntidadNoEncontradaException("El equipo con ID " + id + " no existe"));
        }

        if(dto.estadio() != null){
            Estadio estadioExistente = equipo.getEstadio();
            if(estadioExistente != null){
                estadioMapper.actualizarEstadioDesdeDTO(dto.estadio(), estadioExistente);
            }
        }

        validarEquipo(equipo);
        equipoRepository.save(equipo);

        return equipoMapper.responseDTOToEquipo(equipo);
    }

    public void eliminar(Long id) {
        if(id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un valor positivo.");
        }
        equipoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("El jugador con ID " + id + " no existe"));

        equipoRepository.deleteById(id);
    }

    private void validarEquipo(Equipo equipo) {
        if (equipo == null) throw new IllegalArgumentException("El equipo no puede ser nulo.");

        if (equipo.getNombre() == null || equipo.getNombre().trim().isEmpty()) throw new IllegalArgumentException("El nombre del equipo es obligatorio.");

        if (equipo.getCiudad() == null || equipo.getCiudad().trim().isEmpty()) throw new IllegalArgumentException("La ciudad del equipo es obligatoria.");

        boolean existeEquipoDuplicado = equipoRepository.existsByNombreIgnoreCaseAndCiudadIgnoreCase(
                equipo.getNombre(), equipo.getCiudad());

        //if (existeEquipoDuplicado) throw new IllegalArgumentException("Ya existe un equipo con ese nombre en esa ciudad.");

        if (equipo.getJugadores() != null) {
            equipo.getJugadores().stream()
                    .filter(jugador -> jugador.getId() != null)
                    .filter(jugador -> jugadorRepository.existsByIdAndEquipoIsNotNull(jugador.getId()))
                    .findFirst()
                    .ifPresent(jugador -> {
                        throw new IllegalArgumentException("El jugador " + jugador.getNombre() + " ya pertenece a otro equipo.");
                    });
        }
    }
}
