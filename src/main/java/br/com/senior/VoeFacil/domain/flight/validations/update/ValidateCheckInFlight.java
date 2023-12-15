package br.com.senior.VoeFacil.domain.flight.validations.update;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidateCheckInFlight implements UpdateStatusValidator {

    @Override
    public void validate(FlightEntity flight, FlightStatus status) {
        if (flight.getStatus() == FlightStatus.CHECK_IN) {
            if (!(status == FlightStatus.BOARDING || status == FlightStatus.CANCELED)) {
                throw new ValidationException("Voo com status CHECK_IN só pode ser atualizado para BOARDING ou CANCELED");
            }
        }
    }
}
