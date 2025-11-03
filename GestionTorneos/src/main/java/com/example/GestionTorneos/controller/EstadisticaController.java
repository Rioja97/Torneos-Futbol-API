package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.DTO.estadistica.EstadisticaDetailDTO;
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

    @GetMapping("/jugador/{jugadorId}")
    public ResponseEntity<List<EstadisticaDetailDTO>> obtenerPorJugador(@PathVariable Long jugadorId) {
        return ResponseEntity.ok(estadisticaService.obtenerPorJugador(jugadorId));
    }

    @GetMapping("/jugador/{jugadorId}/torneo/{torneoId}")
    public ResponseEntity<List<EstadisticaDetailDTO>> obtenerPorJugadorYTorneo(
            @PathVariable Long jugadorId,
            @PathVariable Long torneoId) {
        return ResponseEntity.ok(estadisticaService.obtenerPorJugadorYTorneo(jugadorId, torneoId));
    }
}
