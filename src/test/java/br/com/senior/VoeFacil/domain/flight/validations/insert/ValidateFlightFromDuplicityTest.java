package br.com.senior.VoeFacil.domain.flight.validations.insert;

import br.com.senior.VoeFacil.domain.flight.DTO.PostFlightDTO;
import br.com.senior.VoeFacil.domain.flight.FlightRepository;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateFlightFromDuplicityTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private ValidateFlightFromDuplicity validator;

    @Test
    @DisplayName("NOK - Deve lançar exceção quando há duplicidade de voo na mesma data e aeronave")
    void shouldThrowExceptionWhenFlightDuplicityExists() {
        // Arrange
        PostFlightDTO dto = new PostFlightDTO(
                new BigDecimal("500.00"),
                LocalDateTime.now(),
                120,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(flightRepository.existsFlightByDepartureTimeAndAircraft(any(), any())).thenReturn(true);

        // Act and Assert
        var ex = assertThrows(ValidationException.class, () -> validator.validate(dto));
        assertThat(ex).isInstanceOf(ValidationException.class).hasMessage("Essa aeronave já possui voo nesta data!");
    }

    @Test
    @DisplayName("OK - Deve permitir quando não há duplicidade de voo na mesma data e aeronave")
    void shouldAllowWhenNoFlightDuplicityExists() {
        // Arrange
        PostFlightDTO dto = new PostFlightDTO(
                new BigDecimal("500.00"),
                LocalDateTime.now(),
                120,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(flightRepository.existsFlightByDepartureTimeAndAircraft(any(), any())).thenReturn(false);

        // Act and Assert
        assertDoesNotThrow(() -> validator.validate(dto));
    }
}