package br.com.senior.VoeFacil.domain.flight.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PostFlightDTO(
        @NotNull
        String number,
        @NotNull
        BigDecimal basePrice,
        @NotNull
        int availableSeatsAmount,
        @NotNull
        LocalDateTime departureTime,
        int durationMinutes,
        @NotNull
        Long departure_airport_id,
        @NotNull
        Long arrival_airport_id,
        @NotNull
        Long aircraft_id) {
}
