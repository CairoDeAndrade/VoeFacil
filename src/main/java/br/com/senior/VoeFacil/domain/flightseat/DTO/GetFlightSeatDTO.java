package br.com.senior.VoeFacil.domain.flightseat.DTO;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flightseat.FlightSeatEntity;
import br.com.senior.VoeFacil.domain.seat.SeatEntity;

import java.util.UUID;

public record GetFlightSeatDTO(UUID id, FlightEntity flight, int seat, boolean seatAvailability) {

    public GetFlightSeatDTO(FlightSeatEntity flightSeatEntity) {
        this(flightSeatEntity.getId(), flightSeatEntity.getFlight(), flightSeatEntity.getSeat().getSeatNumber(), flightSeatEntity.isSeatAvailability());
    }

}
