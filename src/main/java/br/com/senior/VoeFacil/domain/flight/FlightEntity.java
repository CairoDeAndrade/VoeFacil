package br.com.senior.VoeFacil.domain.flight;

import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.airport.AirportEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "Flight")
@Table(name = "flight")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;
    private BigDecimal basePrice;
    private int availableSeatsAmount;
    private LocalDateTime departureTime;
    private int durationMinutes;
    private String status;

    @ManyToOne
    @JoinColumn(name = "departure_airport_id")
    private AirportEntity departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport_id")
    private AirportEntity arrivalAirport;

    @ManyToOne
    @JoinColumn(name = "aircraft_id")
    private AircraftEntity aircraft;


    public FlightEntity(String number, BigDecimal basePrice, int availableSeatsAmount, LocalDateTime departureTime, int durationMinutes, AirportEntity departureAirport, AirportEntity arrivalAirport, AircraftEntity aircraft) {
        this.number = number;
        this.basePrice = basePrice;
        this.availableSeatsAmount = availableSeatsAmount;
        this.departureTime = departureTime;
        this.durationMinutes = durationMinutes;
        this.status = "SCHEDULED";
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.aircraft = aircraft;
    }

}