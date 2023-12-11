package br.com.senior.VoeFacil.domain.seat;


import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.seat.DTO.PostSeatDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Seat")
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int seatNumber;
    private String seatClass;

    @ManyToOne
    @JoinColumn(name = "aircraft_id")
    private AircraftEntity aircraft;

    public SeatEntity (int seatNumber, String seatClass, AircraftEntity aircraft) {
        this.seatNumber = seatNumber;
        this.seatClass = seatClass;
        this.aircraft = aircraft;
    }

}
