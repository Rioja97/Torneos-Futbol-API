package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.DTO.equipo.EquipoCreateDTO;
import com.example.GestionTorneos.DTO.equipo.EquipoResponseDTO;
import com.example.GestionTorneos.DTO.equipo.EquipoUpdateDTO;
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
    public ResponseEntity<List<EquipoResponseDTO>> listar() {
        return ResponseEntity.ok(equipoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(equipoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EquipoResponseDTO> crear(@RequestBody @Valid EquipoCreateDTO equipo) {
        EquipoResponseDTO nuevoEquipo = equipoService.crear(equipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEquipo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipoResponseDTO> actualizar(@PathVariable Long id, @RequestBody @Valid EquipoUpdateDTO datosActualizados) {
        return ResponseEntity.ok().body(equipoService.actualizar(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        EquipoResponseDTO equipo = equipoService.buscarPorId(id);
        equipoService.eliminar(id);
    }
}