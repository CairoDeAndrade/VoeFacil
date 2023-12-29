package br.com.senior.VoeFacil.domain.seat;


import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.seat.DTO.PostSeatDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity(name = "Seat")
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatTypeEnum seatClass;

    @ManyToOne
    @JoinColumn(name = "aircraft_id")
    private AircraftEntity aircraft;

    public SeatEntity (int seatNumber, SeatTypeEnum seatClass, AircraftEntity aircraft) {
        this.seatNumber = seatNumber;
        this.seatClass = seatClass;
        this.aircraft = aircraft;
    }

}
