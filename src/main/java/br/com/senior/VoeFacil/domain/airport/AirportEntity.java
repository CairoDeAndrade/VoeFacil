package br.com.senior.VoeFacil.domain.airport;

import br.com.senior.VoeFacil.domain.airport.DTO.PostAirportDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Airport")
@Table(name = "airport")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class AirportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cep;
    private String country;
    private String city;
    private String neighborhood;
    private String street;
    private int streetNumber;

    public AirportEntity (PostAirportDTO  airportDTO){
        this.name = airportDTO.name();
        this.cep = airportDTO.cep();
        this.country = airportDTO.country();
        this.city = airportDTO.city();
        this.neighborhood = airportDTO.neighborhood();
        this.street = airportDTO.street();
        this.streetNumber = airportDTO.streetNumber();
    }

}
