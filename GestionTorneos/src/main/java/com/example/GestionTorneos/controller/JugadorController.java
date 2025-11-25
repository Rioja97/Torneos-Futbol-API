package com.example.GestionTorneos.controller;

import com.example.GestionTorneos.DTO.jugador.JugadorCreateDTO;
import com.example.GestionTorneos.DTO.jugador.JugadorResponseDTO;
import com.example.GestionTorneos.DTO.jugador.JugadorUpdateDTO;
import com.example.GestionTorneos.model.Jugador;
import com.example.GestionTorneos.repository.JugadorRepository;
import com.example.GestionTorneos.service.JugadorService;
import jakarta.transaction.Transactional;
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
    @Autowired
    private JugadorRepository jugadorRepository;

    @GetMapping
    public ResponseEntity<List<JugadorResponseDTO>> listarTodos() {
        return ResponseEntity.ok(jugadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JugadorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(jugadorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<JugadorResponseDTO> crear(@RequestBody @Valid JugadorCreateDTO jugador) {
        JugadorResponseDTO nuevoJugador = jugadorService.crear(jugador);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoJugador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JugadorResponseDTO> actualizar(@PathVariable Long id, @RequestBody @Valid JugadorUpdateDTO datosActualizados) {
        return ResponseEntity.ok(jugadorService.actualizar(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        JugadorResponseDTO jugador =  jugadorService.buscarPorId(id);
        jugadorService.eliminar(id);
    }

    @GetMapping("/equipo/{equipoId}")
    @Transactional
    public ResponseEntity<List<Jugador>> obtenerPorEquipo(@PathVariable Long equipoId) {
        return ResponseEntity.ok(jugadorRepository.findByEquipoId(equipoId));
    }
}