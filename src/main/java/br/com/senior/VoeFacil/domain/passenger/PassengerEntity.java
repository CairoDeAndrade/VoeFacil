package br.com.senior.VoeFacil.domain.passenger;

import br.com.senior.VoeFacil.domain.passenger.DTO.PostPassengerDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Passenger")
@Table(name = "passenger")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class PassengerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;

    public PassengerEntity(PostPassengerDTO passengerDTO){
        this.name = passengerDTO.name();
        this.email = passengerDTO.email();
        this.phone = passengerDTO.phone();
        this.birthDate = passengerDTO.birthDate();
    }
}