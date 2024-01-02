package br.com.senior.VoeFacil.domain.seat;

import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.aircraft.AircraftRepository;
import br.com.senior.VoeFacil.domain.seat.DTO.GetSeatDTO;
import br.com.senior.VoeFacil.domain.seat.DTO.PostSeatDTO;

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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    public SeatRepository seatRepository;

    @Mock
    public AircraftRepository aircraftRepository;

    public SeatEntity seat;
    public AircraftEntity aircraft;

    @InjectMocks
    public SeatService seatService;

    @BeforeEach
    void setUp() {
        // Preparing a seat and aircraft Mock
        seat = new SeatEntity();
        seat.setAircraft(new AircraftEntity());

        aircraft = new AircraftEntity();
        aircraft.setId(UUID.randomUUID());
    }

    @Nested
    class CreateSeatTest {

        @Test
        @DisplayName("OK - Deve criar um novo assento")
        void shouldCreateNewSeat() {
            // Arrange
            var dto = new PostSeatDTO(1, SeatTypeEnum.ECONOMY, aircraft.getId());

            when(aircraftRepository.findById(dto.aircraft_id())).thenReturn(Optional.of(aircraft));

            // Act
            GetSeatDTO result = seatService.createSeat(dto);

            // Assert
            verify(aircraftRepository, times(1)).findById(dto.aircraft_id());
            verify(seatRepository, times(1)).save(any(SeatEntity.class));

            assertThat(result).isNotNull();
            assertThat(result.seatNumber()).isEqualTo(dto.seatNumber());
            assertThat(result.seatClass()).isEqualTo(dto.seatClass());
            assertThat(result.aircraft().getId()).isEqualTo(dto.aircraft_id());
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção quando aeronave não encontrada")
        void shouldThrowExceptionWhenAircraftNotFound() {
            // Arrange
            var dto = new PostSeatDTO(1, SeatTypeEnum.ECONOMY, aircraft.getId());

            when(aircraftRepository.findById(dto.aircraft_id())).thenReturn(Optional.empty());

            // Act and Assert
            var ex = assertThrows(ResourceNotFoundException.class, () -> seatService.createSeat(dto));

            assertThat(ex).isInstanceOf(ResourceNotFoundException.class).hasMessage("Aeronave não encontrada");
            verify(aircraftRepository, times(1)).findById(dto.aircraft_id());
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção quando o DTO é nulo")
        void shouldThrowExceptionWhenDTOIsNull() {
            // Arrange
            PostSeatDTO dto = null;

            // Act and Assert
            var ex = assertThrows(ValidationException.class, () -> seatService.createSeat(dto));

            assertThat(ex).isInstanceOf(ValidationException.class).hasMessage("Dados da cadeira não podem ser nulos!");
        }
    }

    @Nested
    class FindAllEntitiesByAircraftTest {

        @Test
        @DisplayName("OK - Deve retornar todos os assentos de uma aeronave")
        void shouldReturnAllSeatsOfAnAircraft() {
            // Arrange
            var seats = List.of(seat, seat);

            when(seatRepository.findAllByAircraft(any(AircraftEntity.class))).thenReturn(seats);

            // Act
            List<SeatEntity> result = seatService.findAllEntitiesByAircraft(aircraft);

            // Assert
            verify(seatRepository, times(1)).findAllByAircraft(any(AircraftEntity.class));
            assertThat(result).isNotNull();
            assertThat(result).hasSize(seats.size());
        }
    }


}

