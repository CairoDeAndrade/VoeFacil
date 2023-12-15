package br.com.senior.VoeFacil.domain.flight.validations.update;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;

public interface UpdateStatusValidator {

    void validate(FlightEntity flight, FlightStatus status);
}
