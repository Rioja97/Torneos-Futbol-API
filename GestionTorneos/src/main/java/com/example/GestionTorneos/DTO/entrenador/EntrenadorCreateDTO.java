package com.example.GestionTorneos.DTO.entrenador;

import jakarta.validation.constraints.NotNull;

public record EntrenadorCreateDTO(
        @NotNull String nombre,
        @NotNull int experiencia,
        @NotNull Long equipoId
) {}
