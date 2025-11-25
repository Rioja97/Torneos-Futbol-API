package com.example.GestionTorneos.DTO.partido;

import java.time.LocalDate;

// DTO simple para listas
public record PartidoResponseDTO(
        Long id,

        // AGREGA ESTOS DOS CAMPOS:
        Long equipoLocalId,
        Long equipoVisitanteId,

        String nombreEquipoLocal,
        String nombreEquipoVisitante,
        String nombreTorneo,
        LocalDate fecha,
        String resultado,
        boolean jugado
) {}