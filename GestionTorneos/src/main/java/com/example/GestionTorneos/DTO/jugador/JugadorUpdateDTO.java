package com.example.GestionTorneos.DTO.jugador;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record JugadorUpdateDTO(
        @Size(min = 2, max = 100) String nombre,
        @Min(10) Integer edad,
        String posicion,
        @Min(1) @Max(99) Integer dorsal,
        Long equipoId
) {}