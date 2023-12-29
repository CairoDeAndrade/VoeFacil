package br.com.senior.VoeFacil.controller;

import br.com.senior.VoeFacil.domain.flight.DTO.GetFlightDTO;
import br.com.senior.VoeFacil.domain.flight.DTO.PostFlightDTO;
import br.com.senior.VoeFacil.domain.flight.DTO.UpdateFlightStatusDTO;
import br.com.senior.VoeFacil.domain.flight.FlightService;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("flight")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @PostMapping
    public ResponseEntity<GetFlightDTO> createFlight(@RequestBody @Valid PostFlightDTO dto, UriComponentsBuilder uriBuilder) {
        var flightDTO = flightService.createFlight(dto);
        var uri = uriBuilder.path("/flight/{id}").buildAndExpand(flightDTO.id()).toUri();
        return ResponseEntity.created(uri).body(flightDTO);
    }

    @PutMapping("update-status/{id}")
    public ResponseEntity<GetFlightDTO> updateFlightStatus(@PathVariable UUID id, @RequestBody UpdateFlightStatusDTO dto) {
        return ResponseEntity.ok(flightService.updateFlightStatus(id, dto));
    }

    @PutMapping("delay/{id}")
    public ResponseEntity<GetFlightDTO> delayFlight(@PathVariable UUID id) {
        return ResponseEntity.ok(flightService.delayFlight(id));
    }

    @GetMapping
    public ResponseEntity<Page<GetFlightDTO>> listAllFlights(Pageable paging) {
        var page = flightService.listAllFlights(paging);
        return ResponseEntity.ok(page);
    }

    @GetMapping("{id}")
    public ResponseEntity<GetFlightDTO> getFlightById(@PathVariable UUID id) {
        var flight = flightService.findFlightById(id);
        return ResponseEntity.ok(flight);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<GetFlightDTO>> getAvailableFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam LocalDate date,
            @RequestParam(required = false) SeatTypeEnum seatType,
            Pageable pageable
    ) {
        var flights = flightService.findAvailableFlights(origin, destination, date, seatType, pageable);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/deals")
    public ResponseEntity<Page<GetFlightDTO>> getDealsFlights(Pageable pageable) {
        var flights = flightService.findDealsFlights(pageable);
        return ResponseEntity.ok(flights);
    }

    @PutMapping("/toggle-deal/{id}")
    public ResponseEntity<GetFlightDTO> toggleFlightDeal(@PathVariable UUID id) {
        var flight = flightService.toggleFlightDeal(id);
        return ResponseEntity.ok(flight);
    }

}