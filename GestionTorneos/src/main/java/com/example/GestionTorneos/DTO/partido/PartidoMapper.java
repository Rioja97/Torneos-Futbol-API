package com.example.GestionTorneos.DTO.partido;

import com.example.GestionTorneos.DTO.estadistica.EstadisticaDetailDTO;
import com.example.GestionTorneos.model.Estadistica;
import com.example.GestionTorneos.model.Partido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // <-- ¡Importante! Para que Spring lo inyecte
public interface PartidoMapper {

    /**
     * Convierte una entidad Partido a un DTO ligero para listas.
     * (Este es el método que tenías, le cambié el nombre).
     */
    @Mapping(source = "equipoLocal.nombre", target = "nombreEquipoLocal")
    @Mapping(source = "equipoVisitante.nombre", target = "nombreEquipoVisitante")
    @Mapping(source = "torneo.nombre", target = "nombreTorneo")
    PartidoResponseDTO partidoToPartidoResponseDTO(Partido partido);

    /**
     * Convierte una entidad Partido a un DTO con todos los detalles.
     * (Este es el método nuevo que necesitas).
     */
    @Mapping(source = "equipoLocal.nombre", target = "nombreEquipoLocal")
    @Mapping(source = "equipoVisitante.nombre", target = "nombreEquipoVisitante")
    @Mapping(source = "torneo.nombre", target = "nombreTorneo")
    @Mapping(source = "estadisticas", target = "estadisticas") // <-- MapStruct usará el método de abajo
    PartidoDetailDTO partidoToPartidoDetailDTO(Partido partido);

    /**
     * Método auxiliar que usa el mapper de arriba para convertir la lista.
     * MapStruct es lo suficientemente inteligente para encontrarlo y usarlo.
     */
    @Mapping(source = "jugador.nombre", target = "nombreJugador")
    EstadisticaDetailDTO estadisticaToEstadisticaDetailDTO(Estadistica estadistica);
}