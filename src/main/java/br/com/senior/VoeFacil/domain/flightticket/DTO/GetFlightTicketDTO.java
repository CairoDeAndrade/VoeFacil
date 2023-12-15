package br.com.senior.VoeFacil.domain.flightticket.DTO;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flightticket.FlightTicketEntity;
import br.com.senior.VoeFacil.domain.passenger.PassengerEntity;
import br.com.senior.VoeFacil.domain.seat.SeatEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetFlightTicketDTO(UUID id, BigDecimal totalPrice, String ticketNumber, LocalDateTime reservationDate, boolean canceled, FlightEntity flight, SeatEntity seat, PassengerEntity passenger) {

   public GetFlightTicketDTO(FlightTicketEntity flightTicketEntity) {
        this(flightTicketEntity.getId(), flightTicketEntity.getTotalPrice(), flightTicketEntity.getTicketNumber(), flightTicketEntity.getReservationDate(), flightTicketEntity.isCanceled(), flightTicketEntity.getFlight(), flightTicketEntity.getSeat(), flightTicketEntity.getPassenger());
    }


}
