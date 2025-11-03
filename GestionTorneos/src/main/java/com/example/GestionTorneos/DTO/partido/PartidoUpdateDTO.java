package com.example.GestionTorneos.DTO.partido;

import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

// NOTA: Todos los campos son "wrapper classes" (Long, LocalDate)
// para que puedan ser nulos. No hay @NotNull.
public record PartidoUpdateDTO(

        Long equipoLocalId,

        Long equipoVisitanteId,

        Long torneoId,

        @FutureOrPresent // Mantenemos la validación si el campo SÍ está presente
        LocalDate fecha
) {
}