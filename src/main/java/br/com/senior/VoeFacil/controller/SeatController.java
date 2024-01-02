package br.com.senior.VoeFacil.controller;

import br.com.senior.VoeFacil.domain.flightseat.DTO.GetFlightSeatDTO;
import br.com.senior.VoeFacil.domain.flightseat.FlightSeatEntity;
import br.com.senior.VoeFacil.domain.flightseat.FlightSeatService;
import br.com.senior.VoeFacil.domain.seat.DTO.GetSeatDTO;
import br.com.senior.VoeFacil.domain.seat.DTO.PostSeatDTO;
import br.com.senior.VoeFacil.domain.seat.SeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("seat")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @Autowired
    private FlightSeatService  flightSeatService;

    @PostMapping
    public ResponseEntity<GetSeatDTO> createSeat(@RequestBody @Valid PostSeatDTO seatDTO, UriComponentsBuilder uriBuilder) {
        var seat = seatService.createSeat(seatDTO);
        var uri = uriBuilder.path("/seat/{id}").buildAndExpand(seat.id()).toUri();
        return ResponseEntity.created(uri).body(seat);
    }

    @GetMapping("/{flightId}")
    public ResponseEntity<List<GetFlightSeatDTO>> getAllSeatsForFlight(@PathVariable UUID flightId) {
        List<GetFlightSeatDTO> seats = flightSeatService.getAllSeatsForFlight(flightId);
        return ResponseEntity.ok(seats);
    }
}
