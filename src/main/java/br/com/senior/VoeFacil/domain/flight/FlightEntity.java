package br.com.senior.VoeFacil.domain.flight;

import br.com.senior.VoeFacil.domain.airport.AirportEntity;
import br.com.senior.VoeFacil.domain.flight.DTO.PostFlightDTO;
import br.com.senior.VoeFacil.domain.seat.SeatEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "flight_has_seat",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "seat_id")
    )
    private List<SeatEntity> seats;


    public FlightEntity(PostFlightDTO dto, AirportEntity departureAirport, AirportEntity arrivalAirport, List<SeatEntity> seats) {
        this.number = dto.number();
        this.basePrice = dto.basePrice();
        this.availableSeatsAmount = seats.size();
        this.departureTime = dto.departureTime();
        this.durationMinutes = dto.durationMinutes();
        this.status = FlightStatus.SCHEDULED;
        this.delayed = false;
        this.deal = false;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.seats = seats;
    }
}