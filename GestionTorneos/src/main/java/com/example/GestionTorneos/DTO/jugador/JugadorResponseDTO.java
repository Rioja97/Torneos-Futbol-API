package com.example.GestionTorneos.DTO.jugador;

public record JugadorResponseDTO(
        Long id,
        String nombre,
        int edad,
        String posicion,
        int dorsal,
        String nombreEquipo
) {}
