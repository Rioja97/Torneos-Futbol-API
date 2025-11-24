package com.example.GestionTorneos.excepcion;

import com.example.GestionTorneos.excepcion.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    //  Entidad no encontrada
    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ResponseEntity<?> handleEntidadNoEncontrada(EntidadNoEncontradaException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    //  Entidad duplicada
    @ExceptionHandler(EntidadRepetidaException.class)
    public ResponseEntity<?> handleEntidadRepetida(EntidadRepetidaException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    //  Cupo máximo alcanzado
    @ExceptionHandler(CupoMaximoException.class)
    public ResponseEntity<?> handleCupoMaximo(CupoMaximoException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    //  Valor nulo no permitido
    @ExceptionHandler(NoNuloException.class)
    public ResponseEntity<?> handleNoNulo(NoNuloException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    //  Valor debe ser positivo
    @ExceptionHandler(ValorPositivoException.class)
    public ResponseEntity<?> handleValorPositivo(ValorPositivoException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    //  Jugador sin estadisticas
    @ExceptionHandler(SinEstadisticasException.class)
    public ResponseEntity<?> handleValorPositivo(SinEstadisticasException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    //  Excepción genérica
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno: " + ex.getMessage());
    }

    //  Metodo reutilizable para construir respuesta JSON
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }

    //  Argumentos inválidos (Validaciones manuales como la del Service)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        // Devolvemos 400 Bad Request (o 409 Conflict si preferís)
        // Esto hará que el frontend sepa que fue un error de validación y no del servidor
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
