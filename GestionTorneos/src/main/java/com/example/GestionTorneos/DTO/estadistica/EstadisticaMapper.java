package com.example.GestionTorneos.DTO.estadistica;

import com.example.GestionTorneos.DTO.estadistica.EstadisticaDetailDTO;
import com.example.GestionTorneos.DTO.partido.PartidoDetailDTO;
import com.example.GestionTorneos.model.Estadistica;
import com.example.GestionTorneos.model.Partido;
import org.mapstruct.Mapping;


public interface EstadisticaMapper {
    // En un EstadisticaMapper.java (o dentro del PartidoMapper)
    @Mapping(source = "jugador.nombre", target = "nombreJugador")
    EstadisticaDetailDTO estadisticaToEstadisticaDetailDTO(Estadistica estadistica);

    // En tu PartidoMapper.java
    @Mapping(source = "equipoLocal.nombre", target = "nombreEquipoLocal")
    @Mapping(source = "equipoVisitante.nombre", target = "nombreEquipoVisitante")
    @Mapping(source = "torneo.nombre", target = "nombreTorneo")
    @Mapping(source = "estadisticas", target = "estadisticas") // MapStruct usará el otro mapper
    PartidoDetailDTO partidoToPartidoDetailDTO(Partido partido);
}
