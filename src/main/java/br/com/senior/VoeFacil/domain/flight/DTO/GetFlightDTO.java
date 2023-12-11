package br.com.senior.VoeFacil.domain.flight.DTO;


import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.airport.AirportEntity;
import br.com.senior.VoeFacil.domain.flight.FlightEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetFlightDTO(Long id, String number, BigDecimal basePrice, int availableSeatsAmount, LocalDateTime departureTime, int durationMinutes, String status, AirportEntity departureAirport, AirportEntity arrivalAirport, AircraftEntity aircraft) {

    public GetFlightDTO(FlightEntity  flightEntity) {
        this(flightEntity.getId(), flightEntity.getNumber(), flightEntity.getBasePrice(), flightEntity.getAvailableSeatsAmount(), flightEntity.getDepartureTime(), flightEntity.getDurationMinutes(), flightEntity.getStatus(), flightEntity.getDepartureAirport(), flightEntity.getArrivalAirport(), flightEntity.getAircraft());
    }
}
