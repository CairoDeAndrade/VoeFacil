package br.com.senior.VoeFacil.domain.flight.validations.update;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ValidateBoardingFlightTest {

    private ValidateBoardingFlight validator;
    private FlightEntity boardingFlight;

    @BeforeEach
    void setUp() {
        validator = new ValidateBoardingFlight();
        boardingFlight = new FlightEntity();
        boardingFlight.setStatus(FlightStatus.BOARDING);
    }

    @Test
    @DisplayName("OK - Deve permitir a mudança para HAPPENING")
    void shouldAllowChangeToHappening() {
        // Act and Assert
        assertDoesNotThrow(() -> validator.validate(boardingFlight, FlightStatus.HAPPENING));
    }

    @Test
    @DisplayName("OK - Deve permitir a mudança para CANCELED")
    void shouldAllowChangeToCanceled() {
        // Act and Assert
        assertDoesNotThrow(() -> validator.validate(boardingFlight, FlightStatus.CANCELED));
    }

    @Test
    @DisplayName("NOK - Deve lançar exceção para outros status")
    void shouldThrowExceptionForOtherStatus() {
        // Act and Assert
        var ex = assertThrows(ValidationException.class,
                () -> validator.validate(boardingFlight, FlightStatus.SCHEDULED));
        assertThat(ex).isInstanceOf(ValidationException.class)
                .hasMessage("Status de um Voo em embarque pode ser mudado apenas para HAPPENING ou CANCELED");
    }
}