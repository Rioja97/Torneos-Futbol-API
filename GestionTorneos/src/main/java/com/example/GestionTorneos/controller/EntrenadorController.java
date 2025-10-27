package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.DTO.entrenador.EntrenadorCreateDTO;
import com.example.GestionTorneos.DTO.entrenador.EntrenadorResponseDTO;
import com.example.GestionTorneos.DTO.entrenador.EntrenadorUpdateDTO;
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
    public ResponseEntity<List<EntrenadorResponseDTO>> listarTodos() {
        return ResponseEntity.ok(entrenadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrenadorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(entrenadorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EntrenadorResponseDTO> crear(@RequestBody @Valid EntrenadorCreateDTO entrenador) {
        EntrenadorResponseDTO nuevoEntrenador = entrenadorService.crear(entrenador);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEntrenador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntrenadorResponseDTO> actualizar(@PathVariable Long id, @RequestBody @Valid EntrenadorUpdateDTO datosActualizados) {
        return ResponseEntity.ok().body(entrenadorService.actualizar(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        EntrenadorResponseDTO entrenador = entrenadorService.buscarPorId(id);
        entrenadorService.eliminar(id);
    }
}