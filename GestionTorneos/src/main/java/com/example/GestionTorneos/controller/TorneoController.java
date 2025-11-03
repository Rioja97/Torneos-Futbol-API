package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.DTO.partido.PartidoCreateDTO;
import com.example.GestionTorneos.DTO.partido.PartidoDetailDTO;
import com.example.GestionTorneos.DTO.partido.PartidoResponseDTO;
import com.example.GestionTorneos.DTO.torneo.TorneoCreateDTO;
import com.example.GestionTorneos.DTO.torneo.TorneoResponseDTO;
import com.example.GestionTorneos.model.Partido;
import com.example.GestionTorneos.model.Torneo;
import com.example.GestionTorneos.service.PartidoService;
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
    @Autowired
    private PartidoService partidoService;

    @GetMapping
    public ResponseEntity<List<TorneoResponseDTO>> listar() {
        return ResponseEntity.ok(torneoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TorneoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(torneoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TorneoResponseDTO> crear(@RequestBody @Valid TorneoCreateDTO torneo) {
        TorneoResponseDTO nuevoTorneo = torneoService.crear(torneo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTorneo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TorneoResponseDTO> actualizar(@PathVariable Long id, @RequestBody @Valid TorneoResponseDTO datosActualizados) {
        return ResponseEntity.ok(torneoService.actualizar(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        TorneoResponseDTO torneo = torneoService.buscarPorId(id);
        torneoService.eliminar(id);
    }

    @GetMapping("/{idTorneo}/partidos")
    public ResponseEntity<List<PartidoResponseDTO>> listarPartidosDeTorneo(@PathVariable Long idTorneo) {
        List<PartidoResponseDTO> partidos = torneoService.listarPartidosDeTorneo(idTorneo);
        return ResponseEntity.ok(partidos);
    }

    @PostMapping("/{idTorneo}/partidos")
    public ResponseEntity<PartidoDetailDTO> crearPartidoEnTorneo(
            @PathVariable Long idTorneo,
            @RequestBody @Valid PartidoCreateDTO dto) {

        PartidoDetailDTO nuevoPartido = partidoService.crearPartidoEnTorneo(idTorneo, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPartido);
    }

    @DeleteMapping("/{idTorneo}/partidos/{idPartido}")
    public void eliminarPartidoDelTorneo(@PathVariable Long idTorneo, @PathVariable Long idPartido) {
        partidoService.eliminarPartidoDeTorneo(idTorneo, idPartido);
    }


}