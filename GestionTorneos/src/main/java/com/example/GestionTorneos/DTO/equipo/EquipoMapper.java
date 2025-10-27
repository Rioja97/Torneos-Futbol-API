package com.example.GestionTorneos.DTO.equipo;

import com.example.GestionTorneos.model.Equipo;
import org.mapstruct.*;

import javax.xml.transform.Source;

@Mapper(componentModel = "spring")
public interface EquipoMapper {

    //Mapeo para creacion de equipo
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "jugadores", ignore = true),
            @Mapping(target = "entrenador", ignore = true)
    })
    Equipo createDTOToEquipo(EquipoCreateDTO dto);

    //Mapeo para listado
    @Mapping(source = "estadio.nombre", target = "nombreEstadio")
    @Mapping(source = "estadio.capacidad", target = "capacidadEstadio")
    EquipoResponseDTO responseDTOToEquipo(Equipo equipo);

    //Mapeo para actualización de equipo
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "jugadores", ignore = true)
    @Mapping(target = "entrenador", ignore = true)
    @Mapping(target = "estadio", ignore = true)
    void updateDTOToEquipo(EquipoUpdateDTO dto, @MappingTarget Equipo equipo);
}
