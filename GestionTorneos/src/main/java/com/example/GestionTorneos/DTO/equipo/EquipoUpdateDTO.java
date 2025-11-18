package com.example.GestionTorneos.DTO.equipo;

import com.example.GestionTorneos.DTO.estadio.EstadioDTO;
import jakarta.validation.constraints.Size;

public record EquipoUpdateDTO(
        @Size(min = 2, max = 100) String nombre,
        @Size(min = 2, max = 100) String ciudad,
        EstadioDTO estadio
) {
}
