package com.example.GestionTorneos.DTO.estadistica;

import com.example.GestionTorneos.DTO.partido.PartidoDetailDTO;
import com.example.GestionTorneos.model.Estadistica;
import com.example.GestionTorneos.model.Partido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EstadisticaMapper {
    @Mapping(source = "jugador.nombre", target = "nombreJugador")
    @Mapping(source = "torneo.id", target = "torneoId")  // ← Esto ya está correcto
    EstadisticaDetailDTO estadisticaToEstadisticaDetailDTO(Estadistica estadistica);

    @Mapping(source = "equipoLocal.nombre", target = "nombreEquipoLocal")
    @Mapping(source = "equipoVisitante.nombre", target = "nombreEquipoVisitante")
    @Mapping(source = "torneo.nombre", target = "nombreTorneo")
    @Mapping(source = "estadisticas", target = "estadisticas")
    PartidoDetailDTO partidoToPartidoDetailDTO(Partido partido);
}