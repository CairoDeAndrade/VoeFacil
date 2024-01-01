package br.com.senior.VoeFacil.domain.flight.validations.insert;

import br.com.senior.VoeFacil.domain.flight.DTO.PostFlightDTO;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ValidateFlightDepartureAntecedenceTest {

    private ValidateFlightDepartureAntecedence validator;
    private PostFlightDTO dto;

    @BeforeEach
    void setUp() {
        validator = new ValidateFlightDepartureAntecedence();
    }

    @Test
    @DisplayName("OK - Deve permitir quando a data de saída do voo tem exatamente 7 dias de antecedência")
    void shouldAllowWhenDepartureTimeIsExactlySevenDaysAntecedence() {
        // Arrange
        dto = new PostFlightDTO(
                new BigDecimal("500.00"),
                LocalDateTime.now().plusDays(7),
                120,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        // Act and Assert
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    @DisplayName("OK - Deve permitir quando a data de saída do voo tem mais de 7 dias de antecedência")
    void shouldAllowWhenDepartureTimeIsMoreThanSevenDaysAntecedence() {
        // Arrange
        PostFlightDTO dto = new PostFlightDTO(
                new BigDecimal("500.00"),
                LocalDateTime.now().plusDays(8),
                120,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        // Act and Assert
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    @DisplayName("NOK - Deve lançar exceção quando a data de saída do voo é menor que 7 dias")
    void shouldThrowExceptionWhenDepartureTimeIsExactlySevenDaysAndOneMillisecondAntecedence() {
        // Arrange
        PostFlightDTO dto = new PostFlightDTO(
                new BigDecimal("500.00"),
                LocalDateTime.now().plusDays(6),
                120,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        // Act and Assert
        var ex = assertThrows(ValidationException.class,
                () -> validator.validate(dto));

        assertThat(ex).isInstanceOf(ValidationException.class)
                .hasMessage("Data de partida do voo deve ser no mínimo 7 dias antes da data atual");
    }
}