package br.com.senior.VoeFacil.domain.flightticket;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightService;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import br.com.senior.VoeFacil.domain.flightticket.DTO.GetFlightTicketDTO;
import br.com.senior.VoeFacil.domain.flightticket.DTO.PostFlightTicketDTO;
import br.com.senior.VoeFacil.domain.passenger.PassengerService;
import br.com.senior.VoeFacil.domain.seat.SeatEntity;
import br.com.senior.VoeFacil.domain.seat.SeatService;
import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;
import br.com.senior.VoeFacil.infra.exception.ResourceNotFoundException;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class FlightTicketService {

    @Autowired
    private FlightTicketRepository flightTicketRepository;

    @Autowired
    private FlightService flightService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private PassengerService passengerService;

    @Transactional(readOnly = true)
    public Page<GetFlightTicketDTO> listAllFlightTickets(Pageable pageable){
        return flightTicketRepository.findAll(pageable).map(GetFlightTicketDTO::new);
    }

    @Transactional
    public GetFlightTicketDTO createFlightTicket(PostFlightTicketDTO dto) {
        var flight = flightService.findFlightEntityById(dto.flightId());
        verifyFlight(flight);

        var seat = seatService.findSeatEntityById(dto.seatId());
        var passenger = passengerService.findPassengerEntityById(dto.passengerId());

        var totalPrice = calculateTotalPrice(flight, seat);

        var flightTicket = new FlightTicketEntity(totalPrice, dto.ticketNumber(), dto.reservationDate(), flight, seat, passenger);

        var flightSeat = flight.getFlightSeats().stream()
                .filter(fs -> fs.isSeatAvailability() && fs.getSeat().getId().equals(seat.getId()))
                .findFirst();

        if (flightSeat.isEmpty()) {
            throw new ValidationException("Assento indiponível!");
        }

        flightSeat.get().setSeatAvailability(false);
        flight.setAvailableSeatsAmount(flight.getAvailableSeatsAmount() - 1);

        flightTicketRepository.save(flightTicket);
        return new GetFlightTicketDTO(flightTicket);
    }

    private void verifyFlight(FlightEntity flight) {
        if (flight.getStatus() != FlightStatus.SCHEDULED) {
            throw new ValidationException("Voo não pode ser agendado pois já é passada a data de um possível agendamento");
        }
        if (flight.getAvailableSeatsAmount() <= 0) {
            throw new ValidationException("Não há assento disponível para este voo");
        }
    }

    private BigDecimal calculateTotalPrice(FlightEntity flight, SeatEntity seat) {
        var totalPrice = flight.getBasePrice();

        if (seat.getSeatClass().equals(SeatTypeEnum.FIRST_CLASS)) {
            totalPrice = totalPrice.add(flight.getBasePrice().multiply(new BigDecimal("0.3")));
        }

        return totalPrice;
    }


    @Transactional(readOnly = true)
    public GetFlightTicketDTO findFlightTicketById(UUID id){
        var flightTicket = flightTicketRepository.getReferenceById(id);
        return new GetFlightTicketDTO(flightTicket);
    }

    @Transactional
    public void cancelTicket(UUID id) {
        var flightTicket = flightTicketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        var flight = flightService.findFlightEntityById(flightTicket.getFlight().getId());

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
