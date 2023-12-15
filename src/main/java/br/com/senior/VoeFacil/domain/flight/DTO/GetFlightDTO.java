package br.com.senior.VoeFacil.domain.flight.DTO;


import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.airport.AirportEntity;
import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetFlightDTO(
        UUID id,
        String number,
        BigDecimal basePrice,
        int availableSeatsAmount,
        LocalDateTime departureTime,
        int durationMinutes,
        FlightStatus status,
        boolean delayed,
        AirportEntity departureAirport,
        AirportEntity arrivalAirport,
        AircraftEntity aircraft) {

    public GetFlightDTO(FlightEntity  flightEntity) {
        this(
                flightEntity.getId(),
                flightEntity.getNumber(),
                flightEntity.getBasePrice(),
                flightEntity.getAvailableSeatsAmount(),
                flightEntity.getDepartureTime(),
                flightEntity.getDurationMinutes(),
                flightEntity.getStatus(),
                flightEntity.isDelayed(),
                flightEntity.getDepartureAirport(),
                flightEntity.getArrivalAirport(),
                flightEntity.getAircraft());
    }
}
