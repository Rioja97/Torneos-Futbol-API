package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.model.Entrenador;
import com.example.GestionTorneos.service.EntrenadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entrenadores")
public class EntrenadorController {

    @Autowired
    private EntrenadorService entrenadorService;

    @GetMapping
    public ResponseEntity<List<Entrenador>> listarTodos() {
        return ResponseEntity.ok(entrenadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrenador> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(entrenadorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Entrenador> crear(@RequestBody @Valid Entrenador entrenador) {
        Entrenador nuevoEntrenador = entrenadorService.crear(entrenador);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEntrenador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrenador> actualizar(@PathVariable Long id, @RequestBody @Valid Entrenador datosActualizados) {
        return ResponseEntity.ok().body(entrenadorService.actualizar(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        Entrenador entrenador = entrenadorService.buscarPorId(id);
        entrenadorService.eliminar(id);
    }
}