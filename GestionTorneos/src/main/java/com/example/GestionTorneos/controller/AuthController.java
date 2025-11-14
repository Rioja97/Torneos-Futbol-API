package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.DTO.user.UserLoginDTO;
import com.example.GestionTorneos.DTO.user.UserResponseDTO;
import com.example.GestionTorneos.excepcion.EntidadNoEncontradaException;
import com.example.GestionTorneos.model.User;
import com.example.GestionTorneos.security.JwtUtil;
import com.example.GestionTorneos.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private UserService userService;

    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginDTO loginRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            User user = userService.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new EntidadNoEncontradaException("No se encontro un usuario con ese nombre"));

            String token = jwtUtil.generateToken(user.getUsername(), user.getRoles().toString());

            return ResponseEntity.ok(new UserResponseDTO(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserResponseDTO("Credenciales invalidas"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserResponseDTO("Error en el servidor"));
        }

    }
}