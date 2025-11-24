package com.example.GestionTorneos.DTO.entrenador;

import com.example.GestionTorneos.model.Entrenador;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EntrenadorMapper {

    //Mapeo para el creado de entrenador
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "equipo", ignore = true)
    Entrenador crearDTOToEntrenador(EntrenadorCreateDTO dto);

    //Mapeo para listado de entrenador
    @Mapping(source = "equipo.nombre", target = "nombreEquipo")
    @Mapping(source = "equipo.id", target = "equipoId") // <--- ¡AGREGÁ ESTA LÍNEA!
    EntrenadorResponseDTO entrenadorToEntrenadorResponseDTO(Entrenador entrenador);

    //Mapeo de actualizacion de entrenador
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "equipo", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntrenadorDesdeDTO(EntrenadorUpdateDTO dto, @MappingTarget Entrenador entrenador);
}
