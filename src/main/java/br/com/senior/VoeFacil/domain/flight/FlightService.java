package br.com.senior.VoeFacil.domain.flight;

import br.com.senior.VoeFacil.domain.aircraft.AircraftRepository;
import br.com.senior.VoeFacil.domain.airport.AirportRepository;
import br.com.senior.VoeFacil.domain.flight.DTO.GetFlightDTO;
import br.com.senior.VoeFacil.domain.flight.DTO.PostFlightDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private AircraftRepository aircraftRepository;


    public Page<GetFlightDTO> listAllFlights(Pageable pageable){
        return flightRepository.findAll(pageable).map(GetFlightDTO::new);
    }

    @Transactional
    public GetFlightDTO createFlight(PostFlightDTO dto){
        var departureAirport = airportRepository.findById(dto.departure_airport_id())
                .orElseThrow();
        var arrivalAirport = airportRepository.findById(dto.arrival_airport_id())
                .orElseThrow();
        var aircraft = aircraftRepository.findById(dto.aircraft_id())
                .orElseThrow();

        var flight = new FlightEntity(dto.number(), dto.basePrice(), dto.availableSeatsAmount(), dto.departureTime(), dto.durationMinutes(), departureAirport, arrivalAirport, aircraft);
        flightRepository.save(flight);
        return new GetFlightDTO(flight);
    }

    public GetFlightDTO findFlightById(Long id){
        var flight = flightRepository.getReferenceById(id);
        return new GetFlightDTO(flight);
    }

}
