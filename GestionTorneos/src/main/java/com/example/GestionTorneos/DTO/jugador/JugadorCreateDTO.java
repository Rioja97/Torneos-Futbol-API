package com.example.GestionTorneos.DTO.jugador;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record JugadorCreateDTO(
        @NotNull String nombre,
        @NotNull @Min(1) int edad,
        @NotNull String posicion,
        @NotNull @Min(1) @Max(99) int dorsal,
        @NotNull Long equipoId
) {}