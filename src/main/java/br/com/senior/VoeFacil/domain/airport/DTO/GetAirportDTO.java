package br.com.senior.VoeFacil.domain.airport.DTO;

import br.com.senior.VoeFacil.domain.airport.AirportEntity;

public record GetAirportDTO(Long id, String name, String cep, String country, String city, String neighborhood, String street, int streetNumber) {

    public GetAirportDTO(AirportEntity airportEntity){
        this(airportEntity.getId(), airportEntity.getName(), airportEntity.getCep(), airportEntity.getCountry(), airportEntity.getCity(), airportEntity.getNeighborhood(), airportEntity.getStreet(), airportEntity.getStreetNumber());
    }
}
