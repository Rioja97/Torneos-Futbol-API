package com.example.GestionTorneos.DTO.torneo;

import com.example.GestionTorneos.model.Torneo;
import jakarta.validation.Valid;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TorneoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "equiposParticipantes", ignore = true)
    @Mapping(target = "partidos", ignore = true)
    Torneo torneoDTOToTorneo(TorneoCreateDTO dto);

    TorneoResponseDTO torneoToTorneoResponseDTO(Torneo torneo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "equiposParticipantes", ignore = true)
    @Mapping(target = "partidos", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarTorneoDesdeDTO(@Valid TorneoResponseDTO torneoUpdateDTO, @MappingTarget Torneo torneo);
}
