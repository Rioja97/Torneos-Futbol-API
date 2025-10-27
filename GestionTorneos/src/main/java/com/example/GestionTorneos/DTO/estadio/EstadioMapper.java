package com.example.GestionTorneos.DTO.estadio;

import com.example.GestionTorneos.model.Estadio;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface EstadioMapper {

    Estadio dtoToEstadio(EstadioDTO estadioDTO);
    EstadioDTO estadioToEstadioDTO(Estadio estadio);

    //Actualizacion parcial del estadio
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEstadioDesdeDTO(EstadioDTO estadioDTO, @MappingTarget Estadio estadio);
}
