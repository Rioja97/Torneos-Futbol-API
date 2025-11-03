package com.example.GestionTorneos.repository;

import com.example.GestionTorneos.model.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    boolean existsByNombreAndDivision(String nombre, String division);
}
