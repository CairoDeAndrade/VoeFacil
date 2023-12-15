package br.com.senior.VoeFacil.domain.flight.validations.update;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidateCompletedFlight implements UpdateStatusValidator {

    @Override
    public void validate(FlightEntity flight, FlightStatus status) {
        if (flight.getStatus() == FlightStatus.COMPLETED) {
            throw new ValidationException("Não é possível alterar o status de um voo já realizado");
        }
    }
}
