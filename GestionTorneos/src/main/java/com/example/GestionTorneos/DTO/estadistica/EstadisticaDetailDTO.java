package com.example.GestionTorneos.DTO.estadistica;

// DTO para mostrar la estadística en el detalle del partido
public record EstadisticaDetailDTO(
        String nombreJugador,
        int goles,
        int asistencias,
        int tarjetasAmarillas,
        int tarjetasRojas
) {
}