package br.com.senior.VoeFacil.domain.aircraft.DTO;

import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;

public record GetAircraftDTO(Long id, String airline, int capacity) {

    public GetAircraftDTO(AircraftEntity aircraftEntity){
        this(aircraftEntity.getId(), aircraftEntity.getAirline(), aircraftEntity.getCapacity());
    }
}
