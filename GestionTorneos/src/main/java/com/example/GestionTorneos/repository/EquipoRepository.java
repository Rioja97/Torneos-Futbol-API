package com.example.GestionTorneos.repository;

import com.example.GestionTorneos.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    boolean existsByNombreIgnoreCaseAndCiudadIgnoreCase(String nombre, String ciudad);
    boolean existsByNombreIgnoreCaseAndCiudadIgnoreCaseAndIdNot(String nombre, String ciudad, Long id);
}
