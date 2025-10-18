package com.example.GestionTorneos.controller;
import com.example.GestionTorneos.dto.ResultadoPartidoDTO;
import com.example.GestionTorneos.model.Partido;
import com.example.GestionTorneos.service.PartidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partidos")
public class PartidoController {

    @Autowired
    private PartidoService partidoService;

    @GetMapping
    public List<Partido> listarTodos() {
        return partidoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partido> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(partidoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Partido> crear(@RequestBody @Valid Partido partido) {
        Partido nuevoPartido = partidoService.crear(partido);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPartido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partido> actualizar(@PathVariable Long id, @RequestBody @Valid Partido datosActualizados) {
        return ResponseEntity.ok().body(partidoService.actualizar(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        Partido partido = partidoService.buscarPorId(id);
        partidoService.eliminar(id);
    }

    @PutMapping("/{id}/resultado")
    public ResponseEntity<?> registrarResultado(@PathVariable Long id, @RequestBody ResultadoPartidoDTO dto) {

        partidoService.registrarResultadoYEstadisticas(id, dto);
        return ResponseEntity.ok().build();
    }
}