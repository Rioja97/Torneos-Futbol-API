package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.DTO.user.UserLoginDTO;
import com.example.GestionTorneos.DTO.user.UserRegisterDTO;
import com.example.GestionTorneos.DTO.user.UserResponseDTO;
import com.example.GestionTorneos.excepcion.EntidadNoEncontradaException;
import com.example.GestionTorneos.model.Rol;
import com.example.GestionTorneos.model.User;
import com.example.GestionTorneos.repository.RolRepository;
import com.example.GestionTorneos.security.JwtUtil;
import com.example.GestionTorneos.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

    public AuthController(JwtUtil jwtUtil,
                          AuthenticationManager authenticationManager,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          RolRepository rolRepository) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginDTO loginRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );

            User user = userService.findByUsername(loginRequest.username())
                    .orElseThrow(() -> new EntidadNoEncontradaException("No se encontro un usuario con ese nombre"));

            String token = jwtUtil.generateToken(user.getUsername(), user.getAuthorities());

            return ResponseEntity.ok(new UserResponseDTO(token, "Autenticación exitosa"));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserResponseDTO(null, "Credenciales invalidas"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserResponseDTO(null, "Error en el servidor: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO nuevo) {

        if(userService.findByUsername(nuevo.username()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new UserResponseDTO(null, "El usuario ya existe")); // MEJORA: Respuesta JSON
        }

        Rol rolEspectador = rolRepository.findByNombre("ROLE_ESPECTADOR")
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontro el rol con ese nombre"));

        Set<Rol> roles = new HashSet<>();
        roles.add(rolEspectador);

        User  user = new User();
        user.setUsername(nuevo.username());
        user.setPassword(passwordEncoder.encode(nuevo.password()));

        user.setRoles(roles);

        userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponseDTO(null, "Usuario registrado exitosamente!"));
    }
}