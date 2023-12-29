package br.com.senior.VoeFacil.domain.flightseat;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.seat.SeatEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "FlightSeat")
@Table(name = "flight_has_seat")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"flight", "seat"})
public class FlightSeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private FlightEntity flight;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private SeatEntity seat;

    private boolean seatAvailability;

    public FlightSeatEntity(FlightEntity flight, SeatEntity seat, boolean seatAvailability) {
        this.flight = flight;
        this.seat = seat;
        this.seatAvailability = seatAvailability;
    }
}
