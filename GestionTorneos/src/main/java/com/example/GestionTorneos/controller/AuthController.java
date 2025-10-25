package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.DTO.user.UserLoginDTO;
import com.example.GestionTorneos.DTO.user.UserResponseDTO;
import com.example.GestionTorneos.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;


    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;

    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginDTO loginRequest) {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String role = null;

        if ("admin".equals(username) && "admin123".equals(password)) {
            role = "ADMIN";
        } else if ("espectador".equals(username) && "espectador123".equals(password)) {
            role = "ESPECTADOR";
        } else {
            return ResponseEntity.status(401).body(new UserResponseDTO("Credenciales inválidas"));
        }

        String token = jwtUtil.generateToken(username, role);
        return ResponseEntity.ok(new UserResponseDTO(token));
    }
}