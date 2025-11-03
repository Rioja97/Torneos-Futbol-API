package com.example.GestionTorneos.DTO.estadistica;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// Un 'record' genera automáticamente los campos, constructor, getters,
// equals(), hashCode() y toString().
public record EstadisticaJugadorDTO(

        @NotNull(message = "El ID del jugador no puede ser nulo")
        Long jugadorId,

        @Min(value = 0, message = "Los goles no pueden ser negativos")
        int goles,

        @Min(value = 0, message = "Las asistencias no pueden ser negativas")
        int asistencias,

        @Min(value = 0, message = "Las tarjetas amarillas no pueden ser negativas")
        int tarjetasAmarillas,

        @Min(value = 0, message = "Las tarjetas rojas no pueden ser negativas")
        int tarjetasRojas
) {
}