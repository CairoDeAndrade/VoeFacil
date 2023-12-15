package br.com.senior.VoeFacil.domain.flight.validations.update;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidateScheduledFlight implements UpdateStatusValidator {

    @Override
    public void validate(FlightEntity flight, FlightStatus status) {
        if (flight.getStatus() == FlightStatus.SCHEDULED) {
            if (!(status == FlightStatus.CHECK_IN || status == FlightStatus.CANCELED)) {
                throw new ValidationException("Status de um Voo agendado pode ser mudado apenas para CHECK_IN, DELAYED ou CANCELED");
            }
        }
    }
}
