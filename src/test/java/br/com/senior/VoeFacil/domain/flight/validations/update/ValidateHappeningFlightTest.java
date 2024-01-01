package br.com.senior.VoeFacil.domain.flight.validations.update;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ValidateHappeningFlightTest {

    private ValidateHappeningFlight validator;
    private FlightEntity happeningFlight;

    @BeforeEach
    void setUp() {
        validator = new ValidateHappeningFlight();
        happeningFlight = new FlightEntity();
        happeningFlight.setStatus(FlightStatus.HAPPENING);
    }

    @Test
    @DisplayName("OK - Deve permitir a mudança para COMPLETED")
    void shouldAllowChangeToCompleted() {
        // Act and Assert
        assertDoesNotThrow(() -> validator.validate(happeningFlight, FlightStatus.COMPLETED));
    }

    @Test
    @DisplayName("NOK - Deve lançar exceção para outros status")
    void shouldThrowExceptionForOtherStatus() {
        // Act and Assert
        var ex = assertThrows(ValidationException.class,
                () -> validator.validate(happeningFlight, FlightStatus.CANCELED));
        assertThat(ex).isInstanceOf(ValidationException.class)
                .hasMessage("Voo em andamento só pode ser atualizado para COMPLETED");
    }
}