package com.example.GestionTorneos.DTO.partido;

import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

public record PartidoUpdateDTO(

        Long equipoLocalId,
        Long equipoVisitanteId,
        Long torneoId,
        @FutureOrPresent LocalDate fecha
) {}