package br.com.senior.VoeFacil.domain.flight.DTO;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.seat.DTO.GetSeatDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetFlightSeatsDetailsDTO {

    UUID id;
    String number;
    int availableSeatsAmount;
    List<GetSeatDTO> seats;

    public GetFlightSeatsDetailsDTO(FlightEntity flight) {
        this.id = flight.getId();
        this.number = flight.getNumber();
        this.availableSeatsAmount = flight.getAvailableSeatsAmount();
        this.seats = flight.getFlightSeats().stream()
                .map(GetSeatDTO::new)
                .toList();
    }
}
