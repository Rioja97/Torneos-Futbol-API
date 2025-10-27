package com.example.GestionTorneos.DTO.entrenador;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record EntrenadorUpdateDTO(
        @Size(min = 2) String nombre,
        @Min(0) Integer experiencia,
        Long equipoId
) {
}
