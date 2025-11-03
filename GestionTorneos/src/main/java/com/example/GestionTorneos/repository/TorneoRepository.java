package com.example.GestionTorneos.repository;

import com.example.GestionTorneos.model.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    boolean existsByNombreAndDivision(String nombre, String division);

    Optional<Torneo> findByNombreAndDivision(String nombre, String division);
}
