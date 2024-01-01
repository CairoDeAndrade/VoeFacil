package br.com.senior.VoeFacil.domain.flight.DTO;


import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.aircraft.DTO.GetAircraftDTO;
import br.com.senior.VoeFacil.domain.airport.AirportEntity;
import br.com.senior.VoeFacil.domain.airport.DTO.GetAirportDTO;
import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetFlightDTO(
        UUID id,
        String number,
        BigDecimal basePrice,
        int availableSeatsAmount,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime departureTime,
        int durationMinutes,
        FlightStatus status,
        boolean delayed,
        boolean deal,
        GetAirportDTO departureAirport,
        GetAirportDTO arrivalAirport,
        GetAircraftDTO aircraft) {

    public GetFlightDTO(FlightEntity flight) {
        this(
                flight.getId(),
                flight.getNumber(),
                flight.getBasePrice(),
                flight.getAvailableSeatsAmount(),
                flight.getDepartureTime(),
                flight.getDurationMinutes(),
                flight.getStatus(),
                flight.isDelayed(),
                flight.isDeal(),
                new GetAirportDTO(flight.getDepartureAirport()),
                new GetAirportDTO(flight.getArrivalAirport()),
                new GetAircraftDTO(flight.getFlightSeats().get(0).getSeat().getAircraft())
        );
    }

    public GetFlightDTO(FlightEntity flight, AircraftEntity aircraft) {
        this(
                flight.getId(),
                flight.getNumber(),
                flight.getBasePrice(),
                flight.getAvailableSeatsAmount(),
                flight.getDepartureTime(),
                flight.getDurationMinutes(),
                flight.getStatus(),
                flight.isDelayed(),
                flight.isDeal(),
                new GetAirportDTO(flight.getDepartureAirport()),
                new GetAirportDTO(flight.getArrivalAirport()),
                new GetAircraftDTO(aircraft)
        );
    }
}
