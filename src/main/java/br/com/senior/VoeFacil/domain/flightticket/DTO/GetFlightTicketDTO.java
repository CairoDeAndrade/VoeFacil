package br.com.senior.VoeFacil.domain.flightticket.DTO;

import br.com.senior.VoeFacil.domain.flight.DTO.GetFlightDTO;
import br.com.senior.VoeFacil.domain.flightticket.FlightTicketEntity;
import br.com.senior.VoeFacil.domain.passenger.DTO.GetPassengerDTO;
import br.com.senior.VoeFacil.domain.seat.DTO.GetSeatDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetFlightTicketDTO(
        UUID id,
        BigDecimal totalPrice,
        String ticketNumber,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime reservationDate,
        boolean canceled,
        GetFlightDTO flight,
        GetSeatDTO seat,
        GetPassengerDTO passenger) {

   public GetFlightTicketDTO(FlightTicketEntity ticket) {
        this(
                ticket.getId(),
                ticket.getTotalPrice(),
                ticket.getTicketNumber(),
                ticket.getReservationDate(),
                ticket.isCanceled(),
                new GetFlightDTO(ticket.getFlight()),
                new GetSeatDTO(ticket.getSeat()),
                new GetPassengerDTO(ticket.getPassenger())
        );
    }


}
