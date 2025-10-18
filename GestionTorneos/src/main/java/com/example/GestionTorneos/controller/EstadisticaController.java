package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.model.Estadistica;
import com.example.GestionTorneos.service.EstadisticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    public EstadisticaController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    // Obtener TODAS las estadísticas de un jugador
    @GetMapping("/jugador/{jugadorId}")
    public ResponseEntity<List<Estadistica>> obtenerPorJugador(@PathVariable Long jugadorId) {
        return ResponseEntity.ok(estadisticaService.obtenerPorJugador(jugadorId));
    }

    // Obtener estadísticas de un jugador en un torneo específico
    @GetMapping("/jugador/{jugadorId}/torneo/{torneoId}")
    public ResponseEntity<List<Estadistica>> obtenerPorJugadorYTorneo(
            @PathVariable Long jugadorId,
            @PathVariable Long torneoId) {
        return ResponseEntity.ok(estadisticaService.obtenerPorJugadorYTorneo(jugadorId, torneoId));
    }
}
