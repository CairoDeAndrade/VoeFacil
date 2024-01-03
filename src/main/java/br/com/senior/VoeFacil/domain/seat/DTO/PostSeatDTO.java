package br.com.senior.VoeFacil.domain.seat.DTO;

import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PostSeatDTO(
        @NotNull
        int seatNumber,
        @NotNull
        SeatTypeEnum seatClass,
        @NotNull
        UUID aircraft_id) {
}
