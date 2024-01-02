package br.com.senior.VoeFacil.domain.flight.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PostFlightDTO (
        @NotNull
        @Positive
        BigDecimal basePrice,
        @NotNull
        LocalDateTime departureTime,
        @NotNull
        @Positive
        int durationMinutes,
        @NotNull
        UUID departureAirportId,
        @NotNull
        UUID arrivalAirportId,
        @NotNull
        UUID aircraftId) {
}
