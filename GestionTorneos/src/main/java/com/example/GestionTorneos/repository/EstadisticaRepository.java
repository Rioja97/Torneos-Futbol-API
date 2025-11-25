package com.example.GestionTorneos.repository;

import com.example.GestionTorneos.model.Estadistica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstadisticaRepository extends JpaRepository<Estadistica, Long> {

    List<Estadistica> findByJugadorId(Long jugadorId);
    List<Estadistica> findByJugadorIdAndPartido_Torneo_Id(Long jugadorId, Long torneoId);
    List<Estadistica> findByTorneoId(Long torneoId);

}