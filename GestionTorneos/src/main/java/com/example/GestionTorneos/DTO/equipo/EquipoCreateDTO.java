package com.example.GestionTorneos.DTO.equipo;

import com.example.GestionTorneos.DTO.estadio.EstadioDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EquipoCreateDTO(
        @NotNull @Size(min = 2, max = 100) String nombre,
        @NotNull @Size(min = 2, max = 100) String ciudad,
        @NotNull @Valid EstadioDTO estadio
        ) {
}
