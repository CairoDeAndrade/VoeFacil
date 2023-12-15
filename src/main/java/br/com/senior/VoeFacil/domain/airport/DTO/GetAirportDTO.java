package br.com.senior.VoeFacil.domain.airport.DTO;

import br.com.senior.VoeFacil.domain.airport.AirportEntity;

import java.util.UUID;

public record GetAirportDTO(UUID id, String name, String code, String country, String city) {

    public GetAirportDTO(AirportEntity airportEntity){
        this(airportEntity.getId(), airportEntity.getName(), airportEntity.getCode(), airportEntity.getCountry(), airportEntity.getCity());
    }
}
