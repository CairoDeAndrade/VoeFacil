package br.com.senior.VoeFacil.domain.flightseat;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flightseat.DTO.GetFlightSeatDTO;
import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightSeatServiceTest {

    @Mock
    public FlightSeatRepository flightSeatRepository;

    public FlightSeatEntity flightSeat;

    @InjectMocks
    public FlightSeatService flightSeatService;

    @BeforeEach
    void setUp() {
        // Preparing a flightSeat Mock
        flightSeat = new FlightSeatEntity();
        flightSeat.setId(UUID.randomUUID());
        flightSeat.setFlight(new FlightEntity());
    }

    @Nested
    class GetAllSeatsForFlightTest {

        @Test
        @DisplayName("OK - Deve retornar todos os assentos para um voo")
        void shouldReturnAllSeatsForAFlight() {
            // Arrange
            var flightId = flightSeat.getId();

            var seats = List.of(
                    new GetFlightSeatDTO(UUID.randomUUID(), true, 1, SeatTypeEnum.ECONOMY),
                    new GetFlightSeatDTO(UUID.randomUUID(), false, 2, SeatTypeEnum.FIRST_CLASS)
            );

            when(flightSeatRepository.findAllByFlightId(flightId)).thenReturn(seats);

            // Act
            List<GetFlightSeatDTO> result = flightSeatService.getAllSeatsForFlight(flightId);

            // Assert
            verify(flightSeatRepository, times(1)).findAllByFlightId(flightId);
            assertThat(result).isNotNull();
            assertThat(result).hasSize(seats.size());
        }
    }

}
