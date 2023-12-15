package br.com.senior.VoeFacil.domain.flight;

import br.com.senior.VoeFacil.domain.aircraft.AircraftRepository;
import br.com.senior.VoeFacil.domain.airport.AirportRepository;
import br.com.senior.VoeFacil.domain.flight.DTO.GetFlightDTO;
import br.com.senior.VoeFacil.domain.flight.DTO.PostFlightDTO;
import br.com.senior.VoeFacil.domain.flight.DTO.UpdateFlightStatusDTO;
import br.com.senior.VoeFacil.domain.flight.validations.update.UpdateStatusValidator;
import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;
import br.com.senior.VoeFacil.infra.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private List<UpdateStatusValidator> updateStatusValidators;

    @Transactional(readOnly = true)
    public Page<GetFlightDTO> listAllFlights(Pageable pageable){
        return flightRepository.findAll(pageable).map(GetFlightDTO::new);
    }

    @Transactional
    public GetFlightDTO createFlight(PostFlightDTO dto){
        var departureAirport = airportRepository.findById(dto.departure_airport_id())
                .orElseThrow(() -> new ResourceNotFoundException("Voo não encontrado!"));

        var arrivalAirport = airportRepository.findById(dto.arrival_airport_id())
                .orElseThrow(() -> new ResourceNotFoundException("Voo não encontrado!"));

        var aircraft = aircraftRepository.findById(dto.aircraft_id())
                .orElseThrow(() -> new ResourceNotFoundException("Voo não encontrado!"));

        var flight = new FlightEntity(dto.number(), dto.basePrice(), aircraft.getCapacity(), dto.departureTime(), dto.durationMinutes(), departureAirport, arrivalAirport, aircraft);
        flightRepository.save(flight);
        return new GetFlightDTO(flight);
    }

    @Transactional(readOnly = true)
    public GetFlightDTO findFlightById(UUID id){
        var flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voo não encontrado!"));

        return new GetFlightDTO(flight);
    }

    @Transactional
    public GetFlightDTO updateFlightStatus(UUID id, UpdateFlightStatusDTO dto) {

        FlightEntity flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voo não encontrado!"));

        validatePossibleUpdateStatus(flight, dto.status());

        flight.setStatus(dto.status());
        flight = flightRepository.save(flight);

        return new GetFlightDTO(flight);
    }

    private void validatePossibleUpdateStatus(FlightEntity flight, FlightStatus status) {
        updateStatusValidators.forEach(v -> v.validate(flight, status));
    }

    @Transactional
    public GetFlightDTO delayFlight(UUID id) {
        FlightEntity flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voo não encontrado!"));
        flight.setDelayed(true);

        return new GetFlightDTO(flight);
    }

    @Transactional(readOnly = true)
    public Page<GetFlightDTO> findAvailableFlights(
            UUID departureAirportId, UUID arrivalAirportId, LocalDate date, SeatTypeEnum seatType, Pageable pageable
    ) {
        Specification<FlightEntity> spec = Specification
                .where(FlightSpecification.byDepartureAirport(departureAirportId))
                .and(FlightSpecification.byArrivalAirport(arrivalAirportId))
                .and(FlightSpecification.byDepartureDate(date))
                .and(FlightSpecification.byNotCanceled());

        return flightRepository.findAll(spec, pageable).map(GetFlightDTO::new);
    }
}
