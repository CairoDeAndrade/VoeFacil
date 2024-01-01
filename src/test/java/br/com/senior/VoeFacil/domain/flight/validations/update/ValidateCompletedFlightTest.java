package br.com.senior.VoeFacil.domain.flight.validations.update;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateCompletedFlightTest {
    private ValidateCompletedFlight validator;
    private FlightEntity completedFlight;

    @BeforeEach
    void setUp() {
        validator = new ValidateCompletedFlight();
        completedFlight = new FlightEntity();
        completedFlight.setStatus(FlightStatus.COMPLETED);
    }

    @Test
    @DisplayName("NOK - Deve lançar exceção para qualquer tentativa de atualização")
    void shouldThrowExceptionForAnyUpdateAttempt() {
        // Act and Assert
        assertThrows(ValidationException.class,
                () -> validator.validate(completedFlight, FlightStatus.BOARDING));
    }

}