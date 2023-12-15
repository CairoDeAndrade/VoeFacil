package br.com.senior.VoeFacil.domain.flight.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PostFlightDTO(
        @NotBlank
        String number,
        @NotNull
        BigDecimal basePrice,
        @NotNull
        LocalDateTime departureTime,
        int durationMinutes,
        @NotNull
        UUID departure_airport_id,
        @NotNull
        UUID arrival_airport_id,
        @NotNull
        UUID aircraft_id) {
}
