package br.com.senior.VoeFacil.domain.flightticket.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostFlightTicketDTO(
        @NotBlank
        String ticketNumber,
        LocalDateTime reservationDate,
        @NotNull
        UUID flightId,
        @NotNull
        UUID seatId,
        @NotNull
        UUID passengerId) {
}
