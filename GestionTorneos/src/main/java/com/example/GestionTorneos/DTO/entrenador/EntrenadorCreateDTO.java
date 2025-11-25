package com.example.GestionTorneos.DTO.entrenador;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EntrenadorCreateDTO(
        @NotNull @Size(min = 2, max = 100, message = "El nombre debe contener entre 2 y 100 caracteres") String nombre,
        @NotNull @Min(value = 0, message = "La experiencia no puede ser menor a 0")
        @Max(value = 60, message = "La experiencia no puede ser mayor a 60") int experiencia,
        @NotNull Long equipoId
) {}
