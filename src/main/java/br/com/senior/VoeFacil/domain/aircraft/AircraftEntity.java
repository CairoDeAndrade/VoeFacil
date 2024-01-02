package br.com.senior.VoeFacil.domain.aircraft;

import br.com.senior.VoeFacil.domain.aircraft.DTO.PostAircraftDTO;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "Aircraft")
@Table(name = "aircraft")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class AircraftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String airline;
    private int capacity;

    AircraftEntity(PostAircraftDTO dto) {
        this.airline = dto.airline();
        this.capacity = dto.capacity();
    }

}
