package com.example.GestionTorneos.DTO.partido;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PartidoCreateDTO(

        @NotNull(message = "El ID del equipo local es obligatorio")
        Long equipoLocalId,

        @NotNull(message = "El ID del equipo visitante es obligatorio")
        Long equipoVisitanteId,

        @NotNull(message = "La fecha es obligatoria")
        @FutureOrPresent(message = "La fecha del partido no puede ser en el pasado")
        LocalDate fecha
) {
}