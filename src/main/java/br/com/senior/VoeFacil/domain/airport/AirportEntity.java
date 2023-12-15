package br.com.senior.VoeFacil.domain.airport;

import br.com.senior.VoeFacil.domain.airport.DTO.PostAirportDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "Airport")
@Table(name = "airport")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class AirportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String code;
    private String country;
    private String city;

    public AirportEntity (PostAirportDTO  airportDTO){
        this.name = airportDTO.name();
        this.code = airportDTO.code();
        this.country = airportDTO.country();
        this.city = airportDTO.city();
    }

}
