package br.com.senior.VoeFacil.domain.passenger;

import br.com.senior.VoeFacil.domain.passenger.DTO.GetPassengerDTO;
import br.com.senior.VoeFacil.domain.passenger.DTO.PostPassengerDTO;

import br.com.senior.VoeFacil.infra.exception.ResourceNotFoundException;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

    @Mock
    public PassengerRepository passengerRepository;

    public PassengerEntity passenger;

    @InjectMocks
    public PassengerService passengerService;

    @BeforeEach
    void setUp() {
        // Preparing a passenger Mock
        passenger = new PassengerEntity();
        passenger.setId(UUID.randomUUID());
    }

    @Nested
    class CreatePassengerTest {

        @Test
        @DisplayName("OK - Deve criar um novo passageiro")
        void shouldCreateNewPassenger() {
            // Arrange
            var dto = new PostPassengerDTO("William", "Nogueira", "william.nogueira@gmail.com", "(47) 98892-5478");

            // Act
            GetPassengerDTO result = passengerService.createPassenger(dto);

            // Assert
            verify(passengerRepository, times(1)).save(any(PassengerEntity.class));

            assertThat(result).isNotNull();
            assertThat(result.firstName()).isEqualTo(dto.firstName());
            assertThat(result.lastName()).isEqualTo(dto.lastName());
            assertThat(result.email()).isEqualTo(dto.email());
            assertThat(result.phone()).isEqualTo(dto.phone());

        }

        @Test
        @DisplayName("NOK - Deve lançar exceção quando o DTO é nulo")
        void shouldThrowExceptionWhenDTOIsNull() {
            // Arrange
            PostPassengerDTO dto = null;

            // Act and Assert
            var ex = assertThrows(ValidationException.class, () -> passengerService.createPassenger(dto));

            assertThat(ex).isInstanceOf(ValidationException.class).hasMessage("Dados do passageiro não podem ser nulos!");
        }
    }

    @Nested
    class FindPassengerByIdTest {

        @Test
        @DisplayName("OK - Deve encontrar passageiro pelo ID")
        void shouldFindPassengerById() {
            // Arrange
            var passengerId = passenger.getId();

            when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));

            // Act
            GetPassengerDTO result = passengerService.findPassengerById(passengerId);

            // Assert
            verify(passengerRepository, times(1)).findById(passengerId);
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(passengerId);
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção para passageiro não encontrado")
        void shouldThrowExceptionForNonExistentPassenger() {
            // Arrange
            var passengerId = UUID.randomUUID();

            when(passengerRepository.findById(passengerId)).thenReturn(Optional.empty());

            // Act and Assert
            var ex = assertThrows(ResourceNotFoundException.class,
                    () -> passengerService.findPassengerById(passengerId));

            assertThat(ex).isInstanceOf(ResourceNotFoundException.class).hasMessage("Passageiro não encontrado!");
            verify(passengerRepository, times(1)).findById(passengerId);
        }
    }

}
