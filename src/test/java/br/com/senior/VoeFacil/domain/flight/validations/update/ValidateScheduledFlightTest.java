package br.com.senior.VoeFacil.domain.flight.validations.update;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ValidateScheduledFlightTest {
    private ValidateScheduledFlight validator;
    private FlightEntity scheduledFlight;

    @BeforeEach
    void setUp() {
        validator = new ValidateScheduledFlight();
        scheduledFlight = new FlightEntity();
        scheduledFlight.setStatus(FlightStatus.SCHEDULED);
    }

    @Test
    @DisplayName("OK - Deve permitir a mudança para CHECK_IN")
    void shouldAllowChangeToCheckIn() {
        // Act and Assert
        assertDoesNotThrow(() -> validator.validate(scheduledFlight, FlightStatus.CHECK_IN));
    }

    @Test
    @DisplayName("OK - Deve permitir a mudança para CANCELED")
    void shouldAllowChangeToCanceled() {
        // Act and Assert
        assertDoesNotThrow(() -> validator.validate(scheduledFlight, FlightStatus.CANCELED));
    }

    @Test
    @DisplayName("NOK - Deve lançar exceção para outros status")
    void shouldThrowExceptionForOtherStatus() {
        // Act and Assert
        var ex = assertThrows(ValidationException.class,
                () -> validator.validate(scheduledFlight, FlightStatus.BOARDING));
        assertThat(ex).isInstanceOf(ValidationException.class)
                .hasMessage("Status de um Voo agendado pode ser mudado apenas para CHECK_IN ou CANCELED");
    }
}