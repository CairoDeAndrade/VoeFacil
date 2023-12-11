package br.com.senior.VoeFacil.domain.seat.DTO;

import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.seat.SeatEntity;

public record GetSeatDTO(Long id, int seatNumber, String seatClass, AircraftEntity aircraft) {
    public GetSeatDTO(SeatEntity seatEntity){
        this(seatEntity.getId(), seatEntity.getSeatNumber(), seatEntity.getSeatClass(), seatEntity.getAircraft());
    }
}
