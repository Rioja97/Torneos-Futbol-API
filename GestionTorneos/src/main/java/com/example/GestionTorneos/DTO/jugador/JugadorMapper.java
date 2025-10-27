package com.example.GestionTorneos.DTO.jugador;

import com.example.GestionTorneos.model.Jugador;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface JugadorMapper {

    //Mapeo para creacion de jugador
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "equipo", ignore = true)
    Jugador crearDTOToJugador(JugadorCreateDTO jugador);

    //Mapeo para listado de jugador
    @Mapping(source = "equipo.nombre", target = "nombreEquipo")
    JugadorResponseDTO jugadorToJugadorResponseDTO(Jugador jugador);

    //Mapeo de actualizacion de jugador
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "equipo", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarJugadorDesdeDTO(JugadorUpdateDTO dto, @MappingTarget Jugador jugador);
}
