package br.com.senior.VoeFacil.controller;

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

import java.util.UUID;

@RestController
@RequestMapping("seat")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping
    public ResponseEntity<Page<GetSeatDTO>> listAllSeats(Pageable paging) {
        var page = seatService.listAllSeats(paging);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<GetSeatDTO> createSeat(@RequestBody @Valid PostSeatDTO seatDTO, UriComponentsBuilder uriBuilder) {
        var seat = seatService.createSeat(seatDTO);
        var uri = uriBuilder.path("/seat/{id}").buildAndExpand(seat.id()).toUri();
        return ResponseEntity.created(uri).body(seat);
    }

    @GetMapping("{id}")
    public ResponseEntity<GetSeatDTO> getSeatById(@PathVariable UUID id) {
        var seat = seatService.findSeatById(id);
        return ResponseEntity.ok(seat);
    }

}
