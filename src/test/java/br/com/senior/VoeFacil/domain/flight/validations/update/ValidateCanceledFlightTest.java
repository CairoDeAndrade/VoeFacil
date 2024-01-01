package br.com.senior.VoeFacil.domain.flight.validations.update;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateCanceledFlightTest {

    private ValidateCanceledFlight validator;
    private FlightEntity canceledFlight;

    @BeforeEach
    void setUp() {
        validator = new ValidateCanceledFlight();
        canceledFlight = new FlightEntity();
        canceledFlight.setStatus(FlightStatus.CANCELED);
    }

    @Test
    @DisplayName("NOK - Deve lançar exceção para qualquer tentativa de atualização")
    void shouldThrowExceptionForAnyUpdateAttempt() {
        // Act and Assert
        assertThrows(ValidationException.class,
                () -> validator.validate(canceledFlight, FlightStatus.BOARDING));
    }
}