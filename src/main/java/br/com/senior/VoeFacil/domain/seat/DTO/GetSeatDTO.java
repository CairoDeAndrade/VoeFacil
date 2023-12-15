package br.com.senior.VoeFacil.domain.seat.DTO;

import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.seat.SeatEntity;
import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;

import java.util.UUID;

public record GetSeatDTO(UUID id, int seatNumber, SeatTypeEnum seatClass, AircraftEntity aircraft) {
    public GetSeatDTO(SeatEntity seatEntity){
        this(seatEntity.getId(), seatEntity.getSeatNumber(), seatEntity.getSeatClass(), seatEntity.getAircraft());
    }
}
