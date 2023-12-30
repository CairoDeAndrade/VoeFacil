package br.com.senior.VoeFacil.domain.flightseat.DTO;

import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;

import java.util.UUID;

public record GetFlightSeatDTO(UUID seatId, boolean seatAvailability, int seatNumber, SeatTypeEnum seatClass) {
}
