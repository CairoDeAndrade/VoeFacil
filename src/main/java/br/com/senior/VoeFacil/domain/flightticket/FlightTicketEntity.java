package br.com.senior.VoeFacil.domain.flightticket;

import br.com.senior.VoeFacil.domain.passenger.PassengerEntity;
import br.com.senior.VoeFacil.domain.seat.SeatEntity;
import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "FlightTicket")
@Table(name = "flight_ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class FlightTicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private BigDecimal totalPrice;
    private String ticketNumber;
    private LocalDateTime reservationDate;
    private boolean canceled;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private FlightEntity flight;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private SeatEntity seat;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private PassengerEntity passenger;

    public FlightTicketEntity (BigDecimal totalPrice, String ticketNumber, FlightEntity flight, SeatEntity seat, PassengerEntity passenger) {
        this.totalPrice = totalPrice;
        this.ticketNumber = ticketNumber;
        this.reservationDate = LocalDateTime.now();
        this.canceled = false;
        this.flight = flight;
        this.seat = seat;
        this.passenger = passenger;
    }

}