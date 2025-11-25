package com.example.GestionTorneos.DTO.torneo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TorneoCreateDTO(
        @NotNull @Size(min = 3, max = 100, message = "El nombre debe contener entre 3 y 100 caracteres")
        String nombre,
        @NotNull @Size(min = 3, max = 100, message = "El nombre debe contener entre 3 y 100 caracteres")
        String division,
        @NotNull @Min(value = 4, message = "El cupo no puede ser menor a 4")
        @Max(value = 50, message = "El cupo no puede ser mayor a 50") int cupo
) {
}
