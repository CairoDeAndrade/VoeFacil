package br.com.senior.VoeFacil.domain.flight.DTO;

import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateFlightStatusDTO (@NotNull FlightStatus status){
}
