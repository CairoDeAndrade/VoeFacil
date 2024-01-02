package br.com.senior.VoeFacil.controller;

import br.com.senior.VoeFacil.domain.flightticket.DTO.GetFlightTicketDTO;
import br.com.senior.VoeFacil.domain.flightticket.DTO.PostFlightTicketDTO;
import br.com.senior.VoeFacil.domain.flightticket.FlightTicketEntity;
import br.com.senior.VoeFacil.domain.flightticket.FlightTicketService;
import br.com.senior.VoeFacil.domain.passenger.PassengerEntity;
import br.com.senior.VoeFacil.infra.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("flight-ticket")
public class FlightTicketController {

    @Autowired
    private FlightTicketService flightTicketService;

    @GetMapping
    public ResponseEntity<Page<GetFlightTicketDTO>> listAllFlightTickets(Pageable paging) {
        var page = flightTicketService.listAllFlightTickets(paging);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<GetFlightTicketDTO> createFlightTicket(@RequestBody @Valid PostFlightTicketDTO dto, UriComponentsBuilder uriBuilder){
        var flightTicket = flightTicketService.createFlightTicket(dto);
        var uri = uriBuilder.path("/flight-ticket/{id}").buildAndExpand(flightTicket.id()).toUri();
        return ResponseEntity.created(uri).body(flightTicket);
    }

    @GetMapping("{id}")
    public ResponseEntity<GetFlightTicketDTO> getFlightTicketById(@PathVariable UUID id) {
        var flightTicket = flightTicketService.findFlightTicketById(id);
        return ResponseEntity.ok(flightTicket);
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<Page<GetFlightTicketDTO>> getTicketsByPassenger(@PathVariable UUID passengerId, @RequestParam(required = false) Pageable paging) {
        Page<GetFlightTicketDTO> tickets = flightTicketService.getTicketsByPassenger(passengerId, paging);
        return ResponseEntity.ok(tickets);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeSession(@PathVariable UUID id) {
        flightTicketService.cancelTicket(id);
        return ResponseEntity.noContent().build();
    }
}
