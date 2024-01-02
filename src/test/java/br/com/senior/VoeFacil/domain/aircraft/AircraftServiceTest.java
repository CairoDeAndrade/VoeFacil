package br.com.senior.VoeFacil.domain.aircraft;

import br.com.senior.VoeFacil.domain.aircraft.DTO.GetAircraftDTO;
import br.com.senior.VoeFacil.domain.aircraft.DTO.PostAircraftDTO;
import br.com.senior.VoeFacil.domain.seat.SeatEntity;
import br.com.senior.VoeFacil.domain.seat.SeatRepository;
import br.com.senior.VoeFacil.infra.exception.ResourceNotFoundException;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AircraftServiceTest {

    @Mock
    public AircraftRepository aircraftRepository;

    @Mock
    public SeatRepository seatRepository;

    @Captor
    public ArgumentCaptor<List<SeatEntity>> seatsCaptor;

    @Captor
    public ArgumentCaptor<AircraftEntity> aircraftCaptor;

    public AircraftEntity aircraft;
    public PostAircraftDTO dto;

    @InjectMocks
    public AircraftService aircraftService;

    @BeforeEach
    void setUp() {
        aircraft = new AircraftEntity();
        dto = new PostAircraftDTO("TAM", 30);
    }

    @Nested
    class createAircraftTest {

        @Test
        @DisplayName("OK - Deve inseir uma nova aeronave com seus assentos correlacionados")
        void shouldInsertANewAircraftWithItsSeats() {
            // Act
            GetAircraftDTO result = aircraftService.createAircraft(dto);

            // Assert
            verify(seatRepository, times(1)).saveAll(seatsCaptor.capture());
            verify(aircraftRepository, times(1)).save(aircraftCaptor.capture());
            assertThat(result).isNotNull();

            var updatedAircraft = aircraftCaptor.getValue();
            var createdSeats = seatsCaptor.getValue();
            assertThat(createdSeats.size()).isEqualTo(updatedAircraft.getCapacity());
            assertThat(createdSeats.get(0).getAircraft()).isEqualTo(updatedAircraft);
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção quando parâmetro DTO for nulo")
        void shouldThrowExceptionWhenParameterIsNull() {
            // Act and Assert
            var ex = assertThrows(ValidationException.class, () -> aircraftService.createAircraft(null));
            assertThat(ex).isInstanceOf(ValidationException.class).hasMessage("Dados da aeronave não podem ser nulos!");
            verifyNoInteractions(aircraftRepository);
            verifyNoInteractions(seatRepository);
        }
    }

    @Nested
    class findAircraftByIdTest {
        @Test
        @DisplayName("OK - Deve retornar uma aeronave pelo ID")
        void shouldReturnAircraftById() {
            // Arrange
            var aircraftId = UUID.randomUUID();
            aircraft.setId(aircraftId);

            when(aircraftRepository.findById(aircraftId)).thenReturn(Optional.of(aircraft));

            // Act
            GetAircraftDTO result = aircraftService.findAircraftById(aircraftId);

            // Assert
            verify(aircraftRepository, times(1)).findById(aircraftId);
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(aircraft.getId());
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção ao não encontrar aeronave pelo ID")
        void shouldThrowExceptionWhenAircraftNotFoundById() {
            // Arrange
            UUID nonExistentAircraftId = UUID.randomUUID();

            when(aircraftRepository.findById(nonExistentAircraftId)).thenReturn(Optional.empty());

            // Act and Assert
            var ex = assertThrows(ResourceNotFoundException.class,
                    () -> aircraftService.findAircraftById(nonExistentAircraftId));

            assertThat(ex).isInstanceOf(ResourceNotFoundException.class).hasMessage("Aeronave não encontrada!");
        }
    }

    @Nested
    class findAircraftEntityByIdTest {
        @Test
        @DisplayName("OK - Deve retornar uma aeronave pelo ID")
        void shouldReturnAircraftById() {
            // Arrange
            var aircraftId = UUID.randomUUID();
            aircraft.setId(aircraftId);

            when(aircraftRepository.findById(aircraftId)).thenReturn(Optional.of(aircraft));

            // Act
            AircraftEntity result = aircraftService.findAircraftEntityById(aircraftId);

            // Assert
            verify(aircraftRepository, times(1)).findById(aircraftId);
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(aircraftId);
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção ao não encontrar aeronave pelo ID")
        void shouldThrowExceptionWhenAircraftNotFoundById() {
            // Arrange
            UUID nonExistentAircraftId = UUID.randomUUID();

            when(aircraftRepository.findById(nonExistentAircraftId)).thenReturn(Optional.empty());

            // Act and Assert
            var ex = assertThrows(ResourceNotFoundException.class,
                    () -> aircraftService.findAircraftById(nonExistentAircraftId));

            assertThat(ex).isInstanceOf(ResourceNotFoundException.class).hasMessage("Aeronave não encontrada!");
        }
    }
}