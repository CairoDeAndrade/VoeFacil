package br.com.senior.VoeFacil.domain.flight.validations.update;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidateBoardingFlight implements UpdateStatusValidator {

    @Override
    public void validate(FlightEntity flight, FlightStatus status) {
        if (flight.getStatus() == FlightStatus.BOARDING) {
            if (!(status == FlightStatus.HAPPENING || status == FlightStatus.CANCELED)) {
                throw new ValidationException("Status de um Voo em embarque pode ser mudado apenas para HAPPENING ou CANCELED");
            }
        }
    }
}
