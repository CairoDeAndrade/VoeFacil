package br.com.senior.VoeFacil.domain.flightticket.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PostFlightTicketDTO(
        @NotBlank
        String ticketNumber,
        LocalDateTime reservationDate,
        @NotNull
        UUID flight_id,
        @NotNull
        UUID seat_id,
        @NotNull
        UUID passenger_id) {
}
