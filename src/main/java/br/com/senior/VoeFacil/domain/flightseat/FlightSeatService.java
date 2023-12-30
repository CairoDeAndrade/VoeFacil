package br.com.senior.VoeFacil.domain.flightseat;

import br.com.senior.VoeFacil.domain.flightseat.DTO.GetFlightSeatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FlightSeatService {

    @Autowired
    private FlightSeatRepository flightSeatRepository;

    @Transactional(readOnly = true)
    public List<GetFlightSeatDTO> getAllSeatsForFlight(UUID flightId) {
        return flightSeatRepository.findAllByFlightId(flightId).stream()
                .map(GetFlightSeatDTO::new)
                .collect(Collectors.toList());
    }
}
