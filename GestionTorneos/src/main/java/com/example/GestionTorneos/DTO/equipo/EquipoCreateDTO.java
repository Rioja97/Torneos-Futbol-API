package com.example.GestionTorneos.dto.equipo;

import com.example.GestionTorneos.dto.estadio.EstadioDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EquipoCreateDTO(
        @NotNull @Size(min = 2, max = 100, message = "El nombre debe contener entre 2 y 100 caracteres") String nombre,
        @NotNull @Size(min = 2, max = 100, message = "La ciudad debe contener entre 2 y 100 caracteres") String ciudad,
        @NotNull @Valid EstadioDTO estadio
        ) {
}
