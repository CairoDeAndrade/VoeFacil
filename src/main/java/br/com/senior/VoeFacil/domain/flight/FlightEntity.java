package br.com.senior.VoeFacil.domain.flight;

import br.com.senior.VoeFacil.domain.airport.AirportEntity;
import br.com.senior.VoeFacil.domain.flight.DTO.PostFlightDTO;
import br.com.senior.VoeFacil.domain.flightseat.FlightSeatEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "Flight")
@Table(name = "flight")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String number;
    private BigDecimal basePrice;
    private int availableSeatsAmount;
    private LocalDateTime departureTime;
    private int durationMinutes;

    @Enumerated(EnumType.STRING)
    private FlightStatus status;
    private boolean delayed;
    private boolean deal;

    @ManyToOne
    @JoinColumn(name = "departure_airport_id")
    private AirportEntity departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport_id")
    private AirportEntity arrivalAirport;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<FlightSeatEntity> flightSeats = new ArrayList<>();


    public FlightEntity(String number, PostFlightDTO dto, AirportEntity departureAirport, AirportEntity arrivalAirport) {
        this.number = number;
        this.basePrice = dto.basePrice();
        this.departureTime = dto.departureTime();
        this.durationMinutes = dto.durationMinutes();
        this.status = FlightStatus.SCHEDULED;
        this.delayed = false;
        this.deal = false;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
    }
}