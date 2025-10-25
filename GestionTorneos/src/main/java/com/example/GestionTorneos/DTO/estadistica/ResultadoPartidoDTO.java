package com.example.GestionTorneos.DTO.estadistica;

import java.util.List;

public class ResultadoPartidoDTO {

    private String resultado;
    private List<EstadisticaJugadorDTO> estadisticasJugadores;

    public ResultadoPartidoDTO() {}

    public String getResultado() {
        return resultado;
    }
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    public List<EstadisticaJugadorDTO> getEstadisticasJugadores() {
        return estadisticasJugadores;
    }
    public void setEstadisticasJugadores(List<EstadisticaJugadorDTO> estadisticasJugadores) {
        this.estadisticasJugadores = estadisticasJugadores;
    }
}
