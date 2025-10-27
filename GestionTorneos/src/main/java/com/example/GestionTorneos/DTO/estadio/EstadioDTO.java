package com.example.GestionTorneos.DTO.estadio;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EstadioDTO(

        Long id,

        @NotBlank(message = "El nombre del estadio no puede ser nulo")
        @Size(min = 3)
        String nombre,

        @NotNull
        @Min(1)
        Integer capacidad
) {
}
