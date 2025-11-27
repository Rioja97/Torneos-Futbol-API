package com.example.GestionTorneos.dto.estadio;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EstadioDTO(

        Long id,

        @NotNull(message = "El nombre del estadio no puede ser nulo")
        @Size(min = 3, message = "El nombre debe contener entre 2 y 100 caracteres") String nombre,

        @NotNull
        @Min(value= 200, message = "La capacidad debe ser mayor a 200")
        @Max(value= 200000, message = "La capacidad debe ser menor a  a 200.000") Integer capacidad
) {
}
