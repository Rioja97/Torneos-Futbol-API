package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.model.Partido;
import com.example.GestionTorneos.model.Torneo;
import com.example.GestionTorneos.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/torneos")
public class TorneoController {

    @Autowired
    private TorneoService torneoService;

    @GetMapping
    public ResponseEntity<List<Torneo>> listar() {
        return ResponseEntity.ok(torneoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Torneo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(torneoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Torneo> crear(@RequestBody @Valid Torneo torneo) {
        Torneo nuevoTorneo = torneoService.crear(torneo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTorneo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Torneo> actualizar(@PathVariable Long id, @RequestBody @Valid Torneo datosActualizados) {
        return ResponseEntity.ok(torneoService.actualizar(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        Torneo torneo = torneoService.buscarPorId(id);
        torneoService.eliminar(id);
    }

    @DeleteMapping("/{idTorneo}/equipos/{idEquipo}")
    public ResponseEntity<Torneo> eliminarEquipo(@PathVariable Long idTorneo, @PathVariable Long idEquipo) {
        Torneo torneoActualizado = torneoService.eliminarEquipoDeTorneo(idTorneo, idEquipo);
        return ResponseEntity.ok(torneoActualizado);
    }

    @PostMapping("/{idTorneo}/equipos")
    public ResponseEntity<Torneo> agregarEquipos(@PathVariable Long idTorneo, @RequestBody List<Long> idsEquipos) {
        Torneo torneoActualizado = torneoService.agregarEquiposAlTorneo(idTorneo, idsEquipos);
        return ResponseEntity.ok(torneoActualizado);
    }

    @PostMapping("/{idTorneo}/partidos")
    public ResponseEntity<List<Partido>> agregarPartidosAlTorneo(
            @PathVariable Long idTorneo,
            @RequestBody List<Long> idPartidos
    ) {
        List<Partido> partidosAgregados = torneoService.agregarPartidos(idTorneo, idPartidos);
        return ResponseEntity.ok(partidosAgregados);
    }


    @DeleteMapping("/{idTorneo}/partidos/{idPartido}")
    public ResponseEntity<Partido> eliminarPartido(@PathVariable Long idTorneo, @PathVariable Long idPartido) {
        torneoService.eliminarPartidoDeTorneo(idTorneo, idPartido);
        return ResponseEntity.ok(new Partido());
    }

    @GetMapping("/{idTorneo}/partidos")
    public ResponseEntity<List<Partido>> listarPartidosDeTorneo(@PathVariable Long idTorneo) {
        List<Partido> partidos = torneoService.listarPartidosDeTorneo(idTorneo);
        return ResponseEntity.ok(partidos);
    }

}