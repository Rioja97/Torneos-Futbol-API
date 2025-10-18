package com.example.GestionTorneos.config;

import com.example.GestionTorneos.security.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // ¡Importante!
import org.springframework.security.config.http.SessionCreationPolicy; // Importa SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()

                        // GET → ESPECTADOR o ADMIN
                        .requestMatchers(HttpMethod.GET, "/equipos/**").hasAnyRole("ESPECTADOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/torneos/**").hasAnyRole("ESPECTADOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/entrenadores/**").hasAnyRole("ESPECTADOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/jugadores/**").hasAnyRole("ESPECTADOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/partidos/**").hasAnyRole("ESPECTADOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/estadisticas/**").hasAnyRole("ESPECTADOR", "ADMIN")

                        // POST, PUT, DELETE → solo ADMIN
                        .requestMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                // === 🔥 Manejadores personalizados de error ===
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                            response.setContentType("application/json");
                            response.getWriter().write("""
                        {
                          "error": "No autenticado",
                          "mensaje": "Debe iniciar sesión o proporcionar un token válido."
                        }
                    """);
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                            response.setContentType("application/json");
                            response.getWriter().write("""
                        {
                          "error": "Acceso denegado",
                          "mensaje": "No tiene permisos para acceder a este recurso."
                        }
                    """);
                        })
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
