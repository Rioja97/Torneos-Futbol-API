package com.example.GestionTorneos.service;

import com.example.GestionTorneos.DTO.estadistica.EstadisticaDetailDTO;
import com.example.GestionTorneos.DTO.estadistica.EstadisticaMapper;
import com.example.GestionTorneos.excepcion.SinEstadisticasException;
import com.example.GestionTorneos.model.Estadistica;
import com.example.GestionTorneos.repository.EstadisticaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadisticaService {

    private final EstadisticaRepository estadisticaRepository;
    private final EstadisticaMapper estadisticaMapper;

    public EstadisticaService(EstadisticaRepository estadisticaRepository,
                              EstadisticaMapper estadisticaMapper) {
        this.estadisticaRepository = estadisticaRepository;
        this.estadisticaMapper = estadisticaMapper;
    }

    public List<EstadisticaDetailDTO> obtenerPorJugador(Long jugadorId) {
        List<Estadistica> estadisticas = estadisticaRepository.findByJugadorId(jugadorId);
        if(estadisticas.isEmpty()) {
            throw new SinEstadisticasException("No existen estadisticas para el jugador");
        }

        return estadisticas.stream()
                .map(estadisticaMapper::estadisticaToEstadisticaDetailDTO)
                .collect(Collectors.toList());
    }

    public List<EstadisticaDetailDTO> obtenerPorJugadorYTorneo(Long jugadorId, Long torneoId) {
        List <Estadistica> estadisticas = estadisticaRepository.findByJugadorIdAndPartido_Torneo_Id(jugadorId, torneoId);
        if (estadisticas.isEmpty()) {
            throw new SinEstadisticasException("No existen estadisticas para el jugador");
        }

        return estadisticas.stream()
                .map(estadisticaMapper::estadisticaToEstadisticaDetailDTO)
                .collect(Collectors.toList());
    }
}