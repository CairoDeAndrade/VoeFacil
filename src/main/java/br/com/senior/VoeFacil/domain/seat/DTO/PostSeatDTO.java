package br.com.senior.VoeFacil.domain.seat.DTO;

import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import jakarta.validation.constraints.NotNull;

public record PostSeatDTO(
        @NotNull
        int seatNumber,
        String seatClass,
        @NotNull
        Long aircraft_id) {
}
