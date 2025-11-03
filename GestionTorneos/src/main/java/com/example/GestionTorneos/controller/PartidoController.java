package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.DTO.partido.*;
import com.example.GestionTorneos.DTO.estadistica.ResultadoPartidoDTO;
import com.example.GestionTorneos.DTO.partido.PartidoResponseDTO;
import com.example.GestionTorneos.service.PartidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partidos")
public class PartidoController {

    private final PartidoService partidoService;

    // Usamos inyección por constructor (mejor práctica que @Autowired)
    public PartidoController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    @GetMapping
    public ResponseEntity<List<PartidoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(partidoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidoDetailDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(partidoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PartidoDetailDTO> crear(@RequestBody @Valid PartidoCreateDTO dto) {
        PartidoDetailDTO nuevoPartido = partidoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPartido);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartidoDetailDTO> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid PartidoUpdateDTO dto) { // <-- Usamos el nuevo DTO
        return ResponseEntity.ok().body(partidoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        partidoService.eliminar(id);
        return ResponseEntity.noContent().build(); // .ok() también es válido
    }

    @PutMapping("/{id}/resultado")
    public ResponseEntity<Void> registrarResultado(@PathVariable Long id, @RequestBody @Valid ResultadoPartidoDTO dto) {
        partidoService.registrarResultadoYEstadisticas(id, dto);
        return ResponseEntity.ok().build();
    }
}