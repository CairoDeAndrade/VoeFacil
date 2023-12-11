package br.com.senior.VoeFacil.domain.flightticket;

import br.com.senior.VoeFacil.domain.flight.FlightRepository;
import br.com.senior.VoeFacil.domain.flightticket.DTO.GetFlightTicketDTO;
import br.com.senior.VoeFacil.domain.flightticket.DTO.PostFlightTicketDTO;
import br.com.senior.VoeFacil.domain.passenger.PassengerRepository;
import br.com.senior.VoeFacil.domain.seat.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Page<GetFlightTicketDTO> listAllFlightTickets(Pageable pageable){
        return flightTicketRepository.findAll(pageable).map(GetFlightTicketDTO::new);
    }

    @Transactional
    public GetFlightTicketDTO createFlightTicket(PostFlightTicketDTO dto){
        var flight = flightRepository.findById(dto.flight_id()).orElseThrow();
        var seat = seatRepository.findById(dto.seat_id()).orElseThrow();
        var passenger = passengerRepository.findById(dto.passenger_id()).orElseThrow();

        var flightTicket = new FlightTicketEntity(dto.totalPrice(), dto.ticketNumber(), dto.reservationDate(), flight, seat, passenger);
        flightTicketRepository.save(flightTicket);
        return new GetFlightTicketDTO(flightTicket);
    }

    public GetFlightTicketDTO findFlightTicketById(Long id){
        var flightTicket = flightTicketRepository.getReferenceById(id);
        return new GetFlightTicketDTO(flightTicket);
    }

}
