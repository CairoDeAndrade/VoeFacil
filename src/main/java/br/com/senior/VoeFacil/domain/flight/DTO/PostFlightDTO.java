package br.com.senior.VoeFacil.domain.flight.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PostFlightDTO (
        @NotBlank
        String number,
        @NotNull
        BigDecimal basePrice,
        @NotNull
        LocalDateTime departureTime,
        @NotNull
        int durationMinutes,
        @NotNull
        UUID departureAirportId,
        @NotNull
        UUID arrivalAirportId,
        @NotNull
        UUID aircraftId) {
}
