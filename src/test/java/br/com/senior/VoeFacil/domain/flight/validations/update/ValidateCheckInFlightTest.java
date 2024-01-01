package br.com.senior.VoeFacil.domain.flight.validations.update;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ValidateCheckInFlightTest {
    private ValidateCheckInFlight validator;
    private FlightEntity checkInFlight;

    @BeforeEach
    void setUp() {
        validator = new ValidateCheckInFlight();
        checkInFlight = new FlightEntity();
        checkInFlight.setStatus(FlightStatus.CHECK_IN);
    }


    @Test
    @DisplayName("OK - Deve permitir a mudança para BOARDING")
    void shouldAllowChangeToBoarding() {
        // Act and Assert
        assertDoesNotThrow(() -> validator.validate(checkInFlight, FlightStatus.BOARDING));
    }

    @Test
    @DisplayName("OK - Deve permitir a mudança para CANCELED")
    void shouldAllowChangeToCanceled() {
        // Act and Assert
        assertDoesNotThrow(() -> validator.validate(checkInFlight, FlightStatus.CANCELED));
    }

    @Test
    @DisplayName("NOK - Deve lançar exceção para outros status")
    void shouldThrowExceptionForOtherStatus() {
        // Act and Assert
        var ex = assertThrows(ValidationException.class,
                () -> validator.validate(checkInFlight, FlightStatus.HAPPENING));
        assertThat(ex).isInstanceOf(ValidationException.class)
                .hasMessage("Voo com status CHECK_IN só pode ser atualizado para BOARDING ou CANCELED");
    }
}