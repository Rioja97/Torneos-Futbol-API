package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.DTO.user.ChangePasswordDTO;
import com.example.GestionTorneos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication; // Para obtener el usuario logueado
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users") // Una ruta base protegida
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO passwordDTO,
                                            Authentication authentication) {
        try {
            String username = authentication.getName();

            userService.changePassword(
                    username,
                    passwordDTO.oldPassword(),
                    passwordDTO.newPassword()
            );

            return ResponseEntity.ok("Contraseña cambiada exitosamente.");

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage()); // 401 Unauthorized
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al cambiar la contraseña.");
        }
    }
}
