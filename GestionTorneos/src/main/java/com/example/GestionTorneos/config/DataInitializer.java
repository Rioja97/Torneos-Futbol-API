package com.example.GestionTorneos.config; // O el paquete donde lo hayas creado

import com.example.GestionTorneos.model.Rol;
import com.example.GestionTorneos.model.User;
import com.example.GestionTorneos.repository.RolRepository;
import com.example.GestionTorneos.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Spring inyectará automáticamente las dependencias que necesitamos
    public DataInitializer(RolRepository rolRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // --- 1. Creación de Roles ---
        // (Buscamos los roles, y si no existen, los creamos)

        Rol rolEspectador = rolRepository.findByNombre("ROLE_ESPECTADOR").orElseGet(() -> {
            Rol nuevoRol = new Rol();
            nuevoRol.setNombre("ROLE_ESPECTADOR");
            return rolRepository.save(nuevoRol);
        });

        Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN").orElseGet(() -> {
            Rol nuevoRol = new Rol();
            nuevoRol.setNombre("ROLE_ADMIN");
            return rolRepository.save(nuevoRol);
        });

        // --- 2. Creación del Usuario Administrador por Defecto ---
        String adminUsername = "admin";

        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername(adminUsername);

            adminUser.setPassword(passwordEncoder.encode("admin123"));

            adminUser.setRoles(Set.of(rolAdmin, rolEspectador));

            userRepository.save(adminUser);
        }
    }
}