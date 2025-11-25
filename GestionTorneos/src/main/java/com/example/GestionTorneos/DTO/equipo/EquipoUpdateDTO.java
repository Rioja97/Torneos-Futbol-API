package com.example.GestionTorneos.DTO.equipo;

import com.example.GestionTorneos.DTO.estadio.EstadioDTO;
import jakarta.validation.constraints.Size;

public record EquipoUpdateDTO(
        @Size(min = 2, max = 100, message = "El nombre debe contener entre 2 y 100 caracteres") String nombre,
        @Size(min = 2, max = 100, message = "La ciudad debe contener entre 2 y 100 caracteres") String ciudad,
        EstadioDTO estadio
) {
}
