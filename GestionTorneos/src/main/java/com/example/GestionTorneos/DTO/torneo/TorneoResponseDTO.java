package com.example.GestionTorneos.DTO.torneo;

public record TorneoResponseDTO(
        Long id,
        String nombre,
        String division,
        Integer cupo
) {
}
