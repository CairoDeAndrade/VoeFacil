package br.com.senior.VoeFacil.domain.flight;

import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.aircraft.AircraftService;
import br.com.senior.VoeFacil.domain.airport.AirportEntity;
import br.com.senior.VoeFacil.domain.airport.AirportService;
import br.com.senior.VoeFacil.domain.flight.DTO.GetFlightDTO;
import br.com.senior.VoeFacil.domain.flight.DTO.PostFlightDTO;
import br.com.senior.VoeFacil.domain.flight.DTO.UpdateFlightStatusDTO;
import br.com.senior.VoeFacil.domain.flight.validations.insert.InsertFlightValidator;
import br.com.senior.VoeFacil.domain.flight.validations.update.UpdateStatusValidator;
import br.com.senior.VoeFacil.domain.flightseat.FlightSeatEntity;
import br.com.senior.VoeFacil.domain.seat.SeatEntity;
import br.com.senior.VoeFacil.domain.seat.SeatService;
import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    public FlightRepository flightRepository;

    @Captor
    public ArgumentCaptor<FlightEntity> flightEntityCaptor;

    @Mock
    public AirportService airportService;

    @Mock
    public AircraftService aircraftService;

    @Mock
    public SeatService seatService;

    @Mock
    public List<InsertFlightValidator> insertFlightValidators;

    @Mock
    public List<UpdateStatusValidator> updateStatusValidators;

    public SeatEntity seat;
    public FlightSeatEntity flightSeat;
    public FlightEntity mockFlightEntity;

    @InjectMocks
    public FlightService flightService;

    @BeforeEach
    void setUp() {
        seat = new SeatEntity();
        seat.setAircraft(new AircraftEntity());

        flightSeat = new FlightSeatEntity();
        flightSeat.setSeat(seat);

        mockFlightEntity = new FlightEntity();
        mockFlightEntity.setDepartureAirport(new AirportEntity());
        mockFlightEntity.setArrivalAirport(new AirportEntity());
        mockFlightEntity.setFlightSeats(List.of(flightSeat));
    }

    @Nested
    class listAllFlightsTest {
        @Test
        @DisplayName("OK - Deve retornar uma página de voos")
        void shouldReturnPageOfFlights() {
            // Arrange
            Pageable pageable = Pageable.unpaged();

            Page<FlightEntity> flightEntityPage = new PageImpl<>(List.of(mockFlightEntity, mockFlightEntity));

            when(flightRepository.findAll(pageable)).thenReturn(flightEntityPage);

            // Act
            Page<GetFlightDTO> result = flightService.listAllFlights(pageable);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
        }

        @Test
        @DisplayName("OK - Deve retornar uma página vazia se não houver voo")
        void shouldReturnEmptyPageIfThereIsNoFlight() {
            // Arrange
            Pageable pageable = Pageable.unpaged();

            when(flightRepository.findAll(pageable)).thenReturn(Page.empty());

            // Act
            Page<GetFlightDTO> result = flightService.listAllFlights(pageable);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
        }
    }

    @Nested
    class createFlightTest {

        private PostFlightDTO dto;

        @BeforeEach
        void setUp() {
            dto = new PostFlightDTO(
                    new BigDecimal("500.00"),
                    LocalDateTime.now(),
                    120,
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    UUID.randomUUID()
            );
        }

        @Test
        @DisplayName("OK - Deve criar um novo voo")
        void shouldCreateANewFlight() {
            // Arrange
            when(airportService.findEntityById(any())).thenReturn(new AirportEntity());
            when(aircraftService.findAircraftEntityById(any())).thenReturn(new AircraftEntity());

            var seats = List.of(new SeatEntity(), new SeatEntity());
            when(seatService.findAllEntitiesByAircraft(any())).thenReturn(seats);

            when(flightRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            var result = flightService.createFlight(dto);

            // Assert
            verify(flightRepository, times(1)).save(flightEntityCaptor.capture());
            verify(insertFlightValidators, times(1)).forEach(any());
            assertThat(result).isNotNull();

            FlightEntity capturedFlight = flightEntityCaptor.getValue();
            assertThat(capturedFlight.getNumber()).isNotNull();

            assertThat(capturedFlight.getAvailableSeatsAmount()).isEqualTo(seats.size());
            assertThat(capturedFlight.getFlightSeats().size()).isEqualTo(seats.size());

            assertThat(capturedFlight.getFlightSeats().get(0).isSeatAvailability()).isTrue();
            assertThat(capturedFlight.getFlightSeats().get(1).isSeatAvailability()).isTrue();
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção quando parâmetro dto for nulo")
        void shouldThrowExceptionWhenDtoParameterIsNull() {
            // Arrange
            dto = null;

            // Act and Assert
            Throwable ex = assertThrows(ValidationException.class, () -> flightService.createFlight(dto));
            assertThat(ex).isInstanceOf(ValidationException.class)
                    .hasMessage("Informações para criação do voo não podem estar nulas!");
            verifyNoInteractions(flightRepository);
        }
    }

    @Nested
    class findFlightByIdTest {

        @Test
        @DisplayName("OK - Deve retornar um voo para um ID existente")
        void shouldReturnValidDtoForExistingId() {
            // Arrange
            var existingId = UUID.randomUUID();

            when(flightRepository.findById(existingId)).thenReturn(Optional.of(mockFlightEntity));

            // Act
            GetFlightDTO result = flightService.findFlightById(existingId);

            // Assert
            verify(flightRepository, times(1)).findById(existingId);
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção para um ID inexistente")
        void shouldThrowExceptionForNonexistentId() {
            // Arrange
            var nonexistentId = UUID.randomUUID();
            when(flightRepository.findById(nonexistentId)).thenReturn(Optional.empty());

            // Act and Assert
            var ex = assertThrows(ResourceNotFoundException.class,
                    () -> flightService.findFlightById(nonexistentId));

            assertThat(ex).isInstanceOf(ResourceNotFoundException.class).hasMessage("Voo não encontrado!");
        }
    }

    @Nested
    class updateFlightStatusTest {

        @Test
        @DisplayName("OK - Deve atualizar o status do voo")
        void shouldUpdateFlightStatus() {
            // Arrange
            var existentId = UUID.randomUUID();
            var dto = new UpdateFlightStatusDTO(FlightStatus.SCHEDULED);

            when(flightRepository.findById(existentId)).thenReturn(Optional.of(mockFlightEntity));
            when(flightRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            GetFlightDTO result = flightService.updateFlightStatus(existentId, dto);

            // Assert
            verify(flightRepository, times(1)).findById(existentId);
            verify(flightRepository, times(1)).save(flightEntityCaptor.capture());
            verify(updateStatusValidators, times(1)).forEach(any());
            assertThat(result).isNotNull();

            FlightEntity flight = flightEntityCaptor.getValue();
            assertThat(flight.getStatus()).isEqualTo(dto.status());
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção para um ID inexistente")
        void shouldThrowExceptionForNonexistentId() {
            // Arrange
            var existentId = UUID.randomUUID();
            var dto = new UpdateFlightStatusDTO(FlightStatus.SCHEDULED);

            when(flightRepository.findById(any())).thenReturn(Optional.empty());

            // Act and Assert
            var ex = assertThrows(ResourceNotFoundException.class,
                    () -> flightService.updateFlightStatus(existentId, dto));

            assertThat(ex).isInstanceOf(ResourceNotFoundException.class).hasMessage("Voo não encontrado!");
            verify(flightRepository, never()).save(any());
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção para um DTO nulo")
        void shouldThrowExceptionForNullStatusInDTO() {
            // Act and Assert
            var ex = assertThrows(ValidationException.class,
                    () -> flightService.updateFlightStatus(UUID.randomUUID(), null));

            assertThat(ex).isInstanceOf(ValidationException.class).hasMessage("Status do voo não pode ser nulo!");
            verifyNoInteractions(flightRepository);
        }
    }

    @Nested
    class delayFlightTest {

        @Test
        @DisplayName("OK - Deve atrasar o voo")
        void shouldDelayFlight() {
            // Arrange
            var flightId = UUID.randomUUID();
            mockFlightEntity.setStatus(FlightStatus.SCHEDULED);

            when(flightRepository.findById(flightId)).thenReturn(Optional.of(mockFlightEntity));
            when(flightRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            GetFlightDTO result = flightService.delayFlight(flightId);

            // Assert
            verify(flightRepository, times(1)).findById(flightId);
            verify(flightRepository, times(1)).save(flightEntityCaptor.capture());
            assertThat(result).isNotNull();
            assertThat(result.delayed()).isTrue();

            var flight = flightEntityCaptor.getValue();
            assertThat(flight.isDelayed()).isTrue();
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção para voo não encontrado")
        void shouldThrowExceptionForNonexistentFlight() {
            // Arrange
            when(flightRepository.findById(any())).thenReturn(Optional.empty());

            // Act and Assert
            var ex = assertThrows(ResourceNotFoundException.class,
                    () -> flightService.delayFlight(any()));
            assertThat(ex).isInstanceOf(ResourceNotFoundException.class).hasMessage("Voo não encontrado!");
            verify(flightRepository, never()).save(any());
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção para voo cancelado")
        void shouldThrowExceptionForCanceledFlight() {
            // Arrange
            UUID canceledFlightId = UUID.randomUUID();
            mockFlightEntity.setStatus(FlightStatus.CANCELED);

            when(flightRepository.findById(canceledFlightId)).thenReturn(Optional.of(mockFlightEntity));

            // Act and Assert
            var ex = assertThrows(ValidationException.class,
                    () -> flightService.delayFlight(canceledFlightId));
            assertThat(ex).isInstanceOf(ValidationException.class)
                    .hasMessage("Voo não pode ser atrasado se já está cancelado!");
            verify(flightRepository, never()).save(any());
        }
    }

    @Nested
    class findAvailableFlightsTest {

        @Test
        @DisplayName("OK - Deve retornar voos disponíveis")
        void shouldReturnAvailableFlights() {
            // Arrange
            String origin = "CityA";
            String destination = "CityB";
            LocalDate date = LocalDate.now();
            SeatTypeEnum seatType = SeatTypeEnum.FIRST_CLASS;
            Pageable pageable = Pageable.unpaged();

            mockFlightEntity.setStatus(FlightStatus.SCHEDULED);
            mockFlightEntity.getFlightSeats().get(0).getSeat().setSeatClass(SeatTypeEnum.FIRST_CLASS);
            var flight1 = mockFlightEntity;
            var flight2 = mockFlightEntity;

            when(flightRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(new PageImpl<>(List.of(flight1, flight2)));

            // Act
            Page<GetFlightDTO> result = flightService.findAvailableFlights(origin, destination, date, seatType, pageable);

            // Assert
            verify(flightRepository, times(1)).findAll(any(Specification.class), eq(pageable));
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
        }

        @Test
        @DisplayName("NOK - Deve retirnar página vazia para cidade de origem não encontrada")
        void shouldThrowExceptionForOriginNotFound() {
            // Arrange
            String nonexistentOrigin = "NonexistentCity";
            String destination = "CityB";
            LocalDate date = LocalDate.now();
            SeatTypeEnum seatType = SeatTypeEnum.FIRST_CLASS;
            Pageable pageable = Pageable.unpaged();

            when(flightRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(Page.empty());

            // Act
            Page<GetFlightDTO> result = flightService.findAvailableFlights(nonexistentOrigin, destination, date, seatType, pageable);

            // Assert
            verify(flightRepository, times(1)).findAll(any(Specification.class), eq(pageable));
            assertThat(result.getContent()).isEmpty();
        }
    }

    @Nested
    class findDealsFlights {

        @Test
        @DisplayName("OK - Deve retornar voos em promoção")
        void shouldReturnDealsFlights() {
            // Arrange
            Pageable pageable = Pageable.unpaged();

            mockFlightEntity.setDeal(true);
            var flight1 = mockFlightEntity;
            var flight2 = mockFlightEntity;

            when(flightRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(new PageImpl<>(List.of(flight1, flight2)));

            // Act
            Page<GetFlightDTO> result = flightService.findDealsFlights(pageable);

            // Assert
            verify(flightRepository, times(1)).findAll(any(Specification.class), eq(pageable));
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
        }

        @Test
        @DisplayName("OK - Deve retornar página vazia se não houver voos em promoção")
        void shouldReturnEmptyPageForNoDealsFlights() {
            // Arrange
            Pageable pageable = Pageable.unpaged();

            when(flightRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(Page.empty());

            // Act
            Page<GetFlightDTO> result = flightService.findDealsFlights(pageable);

            // Assert
            verify(flightRepository, times(1)).findAll(any(Specification.class), eq(pageable));
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
        }
    }

    @Nested
    class toggleFlightDealTest {

        @Test
        @DisplayName("OK - Deve inverter o status de promoção do voo")
        void shouldToggleFlightDeal() {
            // Arrange
            var flightId = UUID.randomUUID();

            var updatedFlight = createFlightEntity(true);
            var oldFlight = createFlightEntity(true);

            when(flightRepository.findById(flightId)).thenReturn(Optional.of(updatedFlight));
            when(flightRepository.save(any(FlightEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            GetFlightDTO result = flightService.toggleFlightDeal(flightId);

            // Assert
            verify(flightRepository, times(1)).findById(flightId);
            verify(flightRepository, times(1)).save(updatedFlight);
            assertThat(result).isNotNull();
            assertThat(result.deal()).isEqualTo(!oldFlight.isDeal());
        }


        @Test
        @DisplayName("NOK - Deve lançar exceção para ID de voo inexistente")
        void shouldThrowExceptionForNonexistentFlightId() {
            // Arrange
            var nonexistentId = UUID.randomUUID();
            when(flightRepository.findById(nonexistentId)).thenReturn(Optional.empty());

            // Act and Assert
            var ex = assertThrows(ResourceNotFoundException.class,
                    () -> flightService.toggleFlightDeal(nonexistentId));

            assertThat(ex).isInstanceOf(ResourceNotFoundException.class).hasMessage("Voo não encontrado!");
            verify(flightRepository, never()).save(any(FlightEntity.class));
        }

        private FlightEntity createFlightEntity(boolean deal) {
            var flightEntity = new FlightEntity();
            flightEntity.setStatus(FlightStatus.SCHEDULED);
            flightEntity.setDeal(deal);

            var flightSeatEntity = new FlightSeatEntity();
            var seatEntity = new SeatEntity();
            seatEntity.setSeatClass(SeatTypeEnum.FIRST_CLASS);
            seatEntity.setAircraft(new AircraftEntity());
            flightSeatEntity.setSeat(seatEntity);

            flightEntity.setFlightSeats(List.of(flightSeatEntity));

            flightEntity.setDepartureAirport(new AirportEntity());
            flightEntity.setArrivalAirport(new AirportEntity());

            return flightEntity;
        }
    }

    @Nested
    class findFlightEntityByIdTest {
        @Test
        @DisplayName("OK - Deve retornar um voo existente pelo ID")
        void shouldReturnExistingFlightById() {
            // Arrange
            var existingFlightId = UUID.randomUUID();
            mockFlightEntity.setId(existingFlightId);
            when(flightRepository.findById(existingFlightId)).thenReturn(Optional.of(mockFlightEntity));

            // Act
            FlightEntity result = flightService.findFlightEntityById(existingFlightId);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(existingFlightId);
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção para ID de voo inexistente")
        void shouldThrowExceptionForNonexistentFlightId() {
            // Arrange
            UUID nonexistentFlightId = UUID.randomUUID();

            when(flightRepository.findById(nonexistentFlightId)).thenReturn(Optional.empty());

            // Act and Assert
            var ex = assertThrows(ResourceNotFoundException.class,
                    () -> flightService.findFlightEntityById(nonexistentFlightId));
            assertThat(ex).isInstanceOf(ResourceNotFoundException.class).hasMessage("Voo não encontrado!");
        }
    }
}