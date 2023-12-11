package br.com.senior.VoeFacil.controller;

import br.com.senior.VoeFacil.domain.flightticket.DTO.GetFlightTicketDTO;
import br.com.senior.VoeFacil.domain.flightticket.DTO.PostFlightTicketDTO;
import br.com.senior.VoeFacil.domain.flightticket.FlightTicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("flightticket")
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
        var uri = uriBuilder.path("/flightticket/{id}").buildAndExpand(flightTicket.id()).toUri();
        return ResponseEntity.created(uri).body(flightTicket);
    }

    @GetMapping("{id}")
    public ResponseEntity<GetFlightTicketDTO> getFlightTicketById(@PathVariable Long id) {
        var flightTicket = flightTicketService.findFlightTicketById(id);
        return ResponseEntity.ok(flightTicket);
    }
}
