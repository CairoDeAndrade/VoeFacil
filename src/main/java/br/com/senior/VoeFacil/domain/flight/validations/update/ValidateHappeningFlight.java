package br.com.senior.VoeFacil.domain.flight.validations.update;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidateHappeningFlight implements UpdateStatusValidator {

    @Override
    public void validate(FlightEntity flight, FlightStatus status) {
        if (flight.getStatus() == FlightStatus.HAPPENING) {
            if (!(status == FlightStatus.COMPLETED)) {
                throw new ValidationException("Voo em andamento só pode ser atualizado para COMPLETED");
            }
        }
    }
}
