package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.model.Equipo;
import com.example.GestionTorneos.service.EquipoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipos")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @GetMapping
    public ResponseEntity<List<Equipo>> listar() {
        return ResponseEntity.ok(equipoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(equipoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Equipo> crear(@RequestBody @Valid Equipo equipo) {
        Equipo nuevoEquipo = equipoService.crear(equipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEquipo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipo> actualizar(@PathVariable Long id, @RequestBody @Valid Equipo datosActualizados) {
        return ResponseEntity.ok().body(equipoService.actualizar(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        Equipo equipo = equipoService.buscarPorId(id);
        equipoService.eliminar(id);
    }
}