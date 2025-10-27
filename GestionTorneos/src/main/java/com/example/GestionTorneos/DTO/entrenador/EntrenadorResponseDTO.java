package com.example.GestionTorneos.DTO.entrenador;

public record EntrenadorResponseDTO(
        Long id,
        String nombre,
        int experiencia,
        String nombreEquipo
) {
}
