package br.com.senior.VoeFacil.domain.flight.validations.insert;

import br.com.senior.VoeFacil.domain.flight.DTO.PostFlightDTO;
import br.com.senior.VoeFacil.domain.flight.FlightRepository;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateFlightFromDuplicity implements InsertFlightValidator {

    @Autowired
    private FlightRepository repository;

    @Override
    public void validate(PostFlightDTO dto) {
        boolean flightDuplicity = repository.existsFlightByDepartureTimeAndAircraft(dto.departureTime(), dto.aircraftId());

        if (flightDuplicity) {
            throw new ValidationException("Essa aeronave já possui voo nesta data!");
        }
    }
}