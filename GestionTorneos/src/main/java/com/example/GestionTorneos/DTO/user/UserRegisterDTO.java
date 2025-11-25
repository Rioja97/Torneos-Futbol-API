package com.example.GestionTorneos.DTO.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterDTO(
        @Size(min = 4, max = 15, message = "El nombre de usuario debe contener entre 4 y 15 caracteres")
        String username,
        @Pattern(
                regexp = ".*\\d.*",
                message = "La contraseña debe contener al menos un número"
        ) String password
) {
}
