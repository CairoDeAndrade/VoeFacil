package br.com.senior.VoeFacil.controller;

import br.com.senior.VoeFacil.domain.aircraft.AircraftService;
import br.com.senior.VoeFacil.domain.aircraft.DTO.GetAircraftDTO;
import br.com.senior.VoeFacil.domain.aircraft.DTO.PostAircraftDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("aircraft")
public class AircraftController {

    @Autowired
    private AircraftService aircraftService;

    @GetMapping
    public ResponseEntity<Page<GetAircraftDTO>> listAllAircrafts(@PageableDefault(size = 10, sort = {"airline"}) Pageable paging) {
        var page = aircraftService.listAllAircraft(paging);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<GetAircraftDTO> createAircraft(@RequestBody @Valid PostAircraftDTO dto, UriComponentsBuilder uriBuilder) {
        var aircraftDTO = aircraftService.createAircraft(dto);
        var uri = uriBuilder.path("/aircraft/{id}").buildAndExpand(aircraftDTO.id()).toUri();
        return ResponseEntity.created(uri).body(aircraftDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<GetAircraftDTO> getAircraftById(@PathVariable Long id) {
        var aircraft = aircraftService.findAircraftById(id);
        return ResponseEntity.ok(aircraft);
    }

}
