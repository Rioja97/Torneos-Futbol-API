package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.model.Jugador;
import com.example.GestionTorneos.service.JugadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jugadores")
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;

    @GetMapping
    public ResponseEntity<List<Jugador>> listarTodos() {
        return ResponseEntity.ok(jugadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jugador> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(jugadorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Jugador> crear(@RequestBody @Valid Jugador jugador) {
        Jugador nuevoJugador = jugadorService.crear(jugador);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoJugador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jugador> actualizar(@PathVariable Long id, @RequestBody @Valid Jugador datosActualizados) {
        return ResponseEntity.ok(jugadorService.actualizar(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        Jugador jugador =  jugadorService.buscarPorId(id);
        jugadorService.eliminar(id);
    }
}