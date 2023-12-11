package br.com.senior.VoeFacil.controller;

import br.com.senior.VoeFacil.domain.airport.AirportService;
import br.com.senior.VoeFacil.domain.airport.DTO.GetAirportDTO;
import br.com.senior.VoeFacil.domain.airport.DTO.PostAirportDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("airport")
public class AirportController {

    @Autowired
    private AirportService  airportService;

    @GetMapping
    public ResponseEntity<Page<GetAirportDTO>> listAllAirports(Pageable paging) {
        var page = airportService.listAllAirports(paging);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<GetAirportDTO> createAirport(@RequestBody @Valid PostAirportDTO dto, UriComponentsBuilder uriBuilder) {
        var airportDTO = airportService.createAirport(dto);
        var uri = uriBuilder.path("/airport/{id}").buildAndExpand(airportDTO.id()).toUri();
        return ResponseEntity.created(uri).body(airportDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<GetAirportDTO> getAirportById(@PathVariable Long id) {
        var airport = airportService.findAirportById(id);
        return ResponseEntity.ok(airport);
    }
}

