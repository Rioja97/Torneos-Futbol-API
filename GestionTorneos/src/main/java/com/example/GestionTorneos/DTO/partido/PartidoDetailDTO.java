package com.example.GestionTorneos.DTO.partido;

import com.example.GestionTorneos.DTO.estadistica.EstadisticaDetailDTO;
import com.example.GestionTorneos.model.Torneo;

import java.time.LocalDate;
import java.util.List;

// DTO completo para la vista de detalle
public record PartidoDetailDTO(
        Long id,
        String nombreEquipoLocal,
        String nombreEquipoVisitante,
        String nombreTorneo,
        LocalDate fecha,
        String resultado,
        boolean jugado,
        List<EstadisticaDetailDTO> estadisticas // <-- La lista de estadísticas
) {
}