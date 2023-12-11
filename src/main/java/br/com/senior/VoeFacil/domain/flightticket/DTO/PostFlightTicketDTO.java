package br.com.senior.VoeFacil.domain.flightticket.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PostFlightTicketDTO(
        @NotNull
        BigDecimal totalPrice,
        @NotBlank
        String ticketNumber,
        LocalDateTime reservationDate,
        @NotNull
        Long flight_id,
        @NotNull
        Long seat_id,
        @NotNull
        Long passenger_id) {
}
