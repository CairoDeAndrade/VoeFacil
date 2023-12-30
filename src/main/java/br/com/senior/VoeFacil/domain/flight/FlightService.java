package br.com.senior.VoeFacil.domain.flight;

import br.com.senior.VoeFacil.domain.aircraft.AircraftService;
import br.com.senior.VoeFacil.domain.airport.AirportService;
import br.com.senior.VoeFacil.domain.flight.DTO.GetFlightDTO;
import br.com.senior.VoeFacil.domain.flight.DTO.GetFlightSeatsDetailsDTO;
import br.com.senior.VoeFacil.domain.flight.DTO.PostFlightDTO;
import br.com.senior.VoeFacil.domain.flight.DTO.UpdateFlightStatusDTO;
import br.com.senior.VoeFacil.domain.flight.validations.insert.InsertFlightValidator;
import br.com.senior.VoeFacil.domain.flight.validations.update.UpdateStatusValidator;
import br.com.senior.VoeFacil.domain.flightseat.FlightSeatEntity;
import br.com.senior.VoeFacil.domain.seat.SeatEntity;
import br.com.senior.VoeFacil.domain.seat.SeatService;
import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;
import br.com.senior.VoeFacil.infra.exception.ResourceNotFoundException;
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
    private AirportService airportService;

    @Autowired
    private AircraftService aircraftService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private List<InsertFlightValidator> insertFlightValidators;

    @Autowired
    private List<UpdateStatusValidator> updateStatusValidators;

    @Transactional(readOnly = true)
    public Page<GetFlightDTO> listAllFlights(Pageable pageable){
        return flightRepository.findAll(pageable).map(GetFlightDTO::new);
    }

    @Transactional
    public GetFlightDTO createFlight(PostFlightDTO dto){
        insertFlightValidators.forEach(v -> v.validate(dto));

        var departureAirport = airportService.findEntityById(dto.departureAirportId());
        var arrivalAirport = airportService.findEntityById(dto.arrivalAirportId());
        var aircraft = aircraftService.findEntityById(dto.aircraftId());
        var seats = seatService.findAllEntitiesByAircraft(aircraft);

        var flight = new FlightEntity(dto, departureAirport, arrivalAirport);

        createFlightSeats(flight, seats);

        flight = flightRepository.save(flight);

        return new GetFlightDTO(flight);
    }

    private void createFlightSeats(FlightEntity flight, List<SeatEntity> seats) {

        for (SeatEntity seat : seats) {
            var flightSeat = new FlightSeatEntity(flight, seat, true);
            flight.getFlightSeats().add(flightSeat);
        }

        flight.setAvailableSeatsAmount(seats.size());
    }

    @Transactional(readOnly = true)
    public GetFlightDTO findFlightById(UUID id){
        var flight = findFlightEntityById(id);
        return new GetFlightDTO(flight);
    }

    @Transactional
    public GetFlightDTO updateFlightStatus(UUID id, UpdateFlightStatusDTO dto) {

        FlightEntity flight = findFlightEntityById(id);

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
        FlightEntity flight = findFlightEntityById(id);
        flight.setDelayed(true);

        return new GetFlightDTO(flight);
    }

    @Transactional(readOnly = true)
    public Page<GetFlightDTO> findAvailableFlights(
            String origin, String destination, LocalDate date, SeatTypeEnum seatType, Pageable pageable
    ) {
        Specification<FlightEntity> spec = Specification
                .where(FlightSpecification.byDepartureAirport(origin))
                .and(FlightSpecification.byArrivalAirport(destination))
                .and(FlightSpecification.byDepartureDate(date))
                .and(FlightSpecification.byNotCanceled()
                .and(FlightSpecification.byStatus(FlightStatus.SCHEDULED)));

        if (seatType != null) {
            spec = spec.and(FlightSpecification.bySeatType(seatType));
        }

        return flightRepository.findAll(spec, pageable).map(GetFlightDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<GetFlightDTO> findDealsFlights(Pageable pageable) {
        return flightRepository.findByDeal(true, pageable).map(GetFlightDTO::new);
    }

    @Transactional
    public GetFlightDTO toggleFlightDeal(UUID id) {
        FlightEntity flight = findFlightEntityById(id);

        flight.setDeal(!flight.isDeal());
        flight = flightRepository.save(flight);

        return new GetFlightDTO(flight);
    }

    @Transactional(readOnly = true)
    public FlightEntity findFlightEntityById(UUID id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voo não encontrado!"));
    }

    @Transactional(readOnly = true)
    public GetFlightSeatsDetailsDTO getFlightSeats(UUID id) {
        var flight = flightRepository.findFlightWithSeatsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voo não encontrado!"));

        return new GetFlightSeatsDetailsDTO(flight);
    }
}
