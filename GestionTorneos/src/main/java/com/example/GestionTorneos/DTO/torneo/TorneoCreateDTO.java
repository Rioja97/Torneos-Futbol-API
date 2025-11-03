package com.example.GestionTorneos.DTO.torneo;

import jakarta.validation.constraints.NotNull;

public record TorneoCreateDTO(
        @NotNull String nombre,
        @NotNull String division,
        @NotNull int cupo
) {
}
