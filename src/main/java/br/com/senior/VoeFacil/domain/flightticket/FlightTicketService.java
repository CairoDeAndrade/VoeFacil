package br.com.senior.VoeFacil.domain.flightticket;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightRepository;
import br.com.senior.VoeFacil.domain.flightticket.DTO.GetFlightTicketDTO;
import br.com.senior.VoeFacil.domain.flightticket.DTO.PostFlightTicketDTO;
import br.com.senior.VoeFacil.domain.passenger.PassengerEntity;
import br.com.senior.VoeFacil.domain.passenger.PassengerRepository;
import br.com.senior.VoeFacil.domain.seat.SeatRepository;
import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;
import br.com.senior.VoeFacil.infra.exception.ResourceNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FlightTicketService {

    @Autowired
    private FlightTicketRepository flightTicketRepository;

    @Autowired
    private FlightRepository  flightRepository;

    @Autowired
    private SeatRepository  seatRepository;

    @Autowired
    private PassengerRepository  passengerRepository;

    @Transactional(readOnly = true)
    public Page<GetFlightTicketDTO> listAllFlightTickets(Pageable pageable){
        return flightTicketRepository.findAll(pageable).map(GetFlightTicketDTO::new);
    }

    @Transactional
    public GetFlightTicketDTO createFlightTicket(PostFlightTicketDTO dto){
        var flight = flightRepository.findById(dto.flight_id()).orElseThrow();
        var seat = seatRepository.findById(dto.seat_id()).orElseThrow();
        var passenger = passengerRepository.findById(dto.passenger_id()).orElseThrow();

        var totalPrice = flight.getBasePrice();

        if (flight.getAvailableSeatsAmount() < 0) {
            throw new ValidationException("No available seats for this flight");
        }

        if (seat.getSeatClass().equals(SeatTypeEnum.FIRST_CLASS)) {
            totalPrice = totalPrice.add(flight.getBasePrice().multiply(new BigDecimal("0.3")));
        }

        var flightTicket = new FlightTicketEntity(totalPrice, dto.ticketNumber(), dto.reservationDate(), flight, seat, passenger);
        flight.setAvailableSeatsAmount(flight.getAvailableSeatsAmount() - 1);

        flightTicketRepository.save(flightTicket);
        return new GetFlightTicketDTO(flightTicket);
    }

    @Transactional(readOnly = true)
    public GetFlightTicketDTO findFlightTicketById(UUID id){
        var flightTicket = flightTicketRepository.getReferenceById(id);
        return new GetFlightTicketDTO(flightTicket);
    }

    @Transactional
    public void cancelTicket(UUID id) {
        var flightTicket = flightTicketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        var flight = flightRepository.findById(flightTicket.getFlight().getId()).orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        flight.setAvailableSeatsAmount(flight.getAvailableSeatsAmount() + 1);
        flightTicket.setCanceled(true);

        flightTicketRepository.save(flightTicket);
    }

    @Transactional(readOnly = true)
    public Page<GetFlightTicketDTO> getTicketsByPassenger(UUID passengerId, Pageable paging) {
        if (paging == null) {
            paging = PageRequest.of(0, 10);
        }
        Page<FlightTicketEntity> tickets = flightTicketRepository.findByPassengerId(passengerId, paging);
        return tickets.map(GetFlightTicketDTO::new);
    }

}
