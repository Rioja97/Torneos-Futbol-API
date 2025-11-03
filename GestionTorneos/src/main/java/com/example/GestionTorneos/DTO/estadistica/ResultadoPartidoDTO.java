package com.example.GestionTorneos.DTO.estadistica;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ResultadoPartidoDTO(

        @NotBlank(message = "El resultado no puede estar vacío")
        String resultado, // Ej: "2-1"

        @NotNull(message = "La lista de estadísticas no puede ser nula")
        @Valid // <-- Importante: le dice a Spring que valide los DTOs dentro de esta lista
        List<EstadisticaJugadorDTO> estadisticasJugadores
) {
}