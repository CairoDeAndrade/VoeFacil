package br.com.senior.VoeFacil.domain.seat.DTO;

import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PostSeatDTO(
        @NotNull
        int seatNumber,
        @NotBlank
        SeatTypeEnum seatClass,
        @NotNull
        UUID aircraft_id) {
}
