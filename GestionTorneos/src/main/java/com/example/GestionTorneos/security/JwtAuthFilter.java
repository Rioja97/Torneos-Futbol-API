package com.example.GestionTorneos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path =  request.getServletPath();
        if(path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Continúa con otros filtros si no hay token o no es Bearer
            return; // Importante para evitar procesar la cadena sin autenticación
        }

        String token = authHeader.substring(7); // Extrae el token
        if (jwtUtil.isTokenValid(token)) {
            String username = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);

            // Spring Security espera roles con el prefijo "ROLE_"
            // Por ejemplo, si tu token dice "ADMIN", Spring Security espera "ROLE_ADMIN"
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username, null, authorities); // Pasa los roles aquí
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Establece la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}