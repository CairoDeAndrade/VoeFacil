package br.com.senior.VoeFacil.domain.aircraft.DTO;

import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;

import java.util.UUID;

public record GetAircraftDTO(UUID id, String airline, int capacity) {

    public GetAircraftDTO(AircraftEntity aircraftEntity){
        this(aircraftEntity.getId(), aircraftEntity.getAirline(), aircraftEntity.getCapacity());
    }
}
