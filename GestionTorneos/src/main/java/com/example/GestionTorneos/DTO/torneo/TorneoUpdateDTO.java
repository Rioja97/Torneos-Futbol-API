package com.example.GestionTorneos.DTO.torneo;

public record TorneoUpdateDTO(
        String nombre,
        String division,
        Integer cupo
) {
}
