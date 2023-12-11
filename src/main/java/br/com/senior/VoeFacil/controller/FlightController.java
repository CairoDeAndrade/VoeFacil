package br.com.senior.VoeFacil.controller;

import br.com.senior.VoeFacil.domain.flight.DTO.GetFlightDTO;
import br.com.senior.VoeFacil.domain.flight.DTO.PostFlightDTO;
import br.com.senior.VoeFacil.domain.flight.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("flight")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping
    public ResponseEntity<Page<GetFlightDTO>> listAllFlights(Pageable paging) {
        var page = flightService.listAllFlights(paging);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<GetFlightDTO> createFlight(@RequestBody @Valid PostFlightDTO dto, UriComponentsBuilder uriBuilder) {
        var flightDTO = flightService.createFlight(dto);
        var uri = uriBuilder.path("/flight/{id}").buildAndExpand(flightDTO.id()).toUri();
        return ResponseEntity.created(uri).body(flightDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<GetFlightDTO> getFlightById(@PathVariable Long id) {
        var flight = flightService.findFlightById(id);
        return ResponseEntity.ok(flight);
    }
}
