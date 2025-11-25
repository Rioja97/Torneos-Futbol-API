package com.example.GestionTorneos.DTO.jugador;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JugadorCreateDTO(
        @NotNull @Size(min = 2, max = 100, message = "El nombre debe " +
                "contener entre 2 y 100 caracteres") String nombre,
        @NotNull @Min(value = 10, message = "La edad no puede ser menor a 10")
        @Max(value = 90, message = "La edad no puede ser mayor a 90") int edad,
        @NotNull @Size(min = 3, max = 50, message = "La posicion debe contener entre 2 y 50 caracteres")
        String posicion,
        @NotNull @Min(value = 1, message = "El dorsal no puede ser menor a 1")
        @Max(value = 99, message = "El no puede ser mayor a 99") int dorsal,
        @NotNull Long equipoId
) {}