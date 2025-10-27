package com.example.GestionTorneos.DTO.equipo;

public record EquipoResponseDTO(
        Long id,
        String nombre,
        String ciudad,
        String nombreEstadio,
        int capacidadEstadio
) {
}
