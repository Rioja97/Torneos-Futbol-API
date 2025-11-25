package com.example.GestionTorneos.repository;

import com.example.GestionTorneos.model.Equipo;
import com.example.GestionTorneos.model.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PartidoRepository extends JpaRepository<Partido, Long> {

    boolean existsByFechaAndEquipoLocalId(LocalDate fecha, Long equipoId);
    boolean existsByFechaAndEquipoVisitanteId(LocalDate fecha, Long equipoId);



    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Partido p " +
            "WHERE p.fecha = :fecha " +
            "AND p.id <> :partidoId " +
            "AND (p.equipoLocal.id = :equipoLocalId OR p.equipoVisitante.id = :equipoVisitanteId)")
    boolean existsByFechaAndEquiposExcluyendoId(@Param("fecha") LocalDate fecha,
                                                @Param("equipoLocalId") Long equipoLocalId,
                                                @Param("equipoVisitanteId") Long equipoVisitanteId,
                                                @Param("partidoId") Long partidoId);

    @Query("""
    SELECT p FROM Partido p
    JOIN FETCH p.equipoLocal
    JOIN FETCH p.equipoVisitante
    JOIN FETCH p.torneo
    LEFT JOIN FETCH p.estadisticas e
    LEFT JOIN FETCH e.jugador
    WHERE p.id = :id
    """)
    Optional<Partido> findByIdConTodo(@Param("id") Long id);


}

