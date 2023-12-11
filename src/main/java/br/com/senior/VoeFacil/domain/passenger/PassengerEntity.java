package br.com.senior.VoeFacil.domain.passenger;

import br.com.senior.VoeFacil.domain.passenger.DTO.PostPassengerDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "Passenger")
@Table(name = "passenger")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class PassengerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private LocalDateTime birthDate;

    public PassengerEntity(PostPassengerDTO passengerDTO){
        this.name = passengerDTO.name();
        this.email = passengerDTO.email();
        this.phone = passengerDTO.phone();
        this.birthDate = passengerDTO.birthDate();
    }
}