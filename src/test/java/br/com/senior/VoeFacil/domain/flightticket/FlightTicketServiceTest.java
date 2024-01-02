package br.com.senior.VoeFacil.domain.flightticket;

import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.airport.AirportEntity;
import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flight.FlightService;
import br.com.senior.VoeFacil.domain.flight.FlightStatus;
import br.com.senior.VoeFacil.domain.flightseat.FlightSeatEntity;
import br.com.senior.VoeFacil.domain.flightticket.DTO.GetFlightTicketDTO;
import br.com.senior.VoeFacil.domain.flightticket.DTO.PostFlightTicketDTO;
import br.com.senior.VoeFacil.domain.passenger.PassengerEntity;
import br.com.senior.VoeFacil.domain.passenger.PassengerService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightTicketServiceTest {

    @Mock
    public FlightTicketRepository flightTicketRepository;

    @Mock
    public FlightService flightService;

    @Mock
    public SeatService seatService;

    @Mock
    public PassengerService passengerService;

    @Captor
    public ArgumentCaptor<FlightTicketEntity> ticketCaptor;

    public FlightTicketEntity flightTicketMock;
    public SeatEntity seat;
    public FlightSeatEntity flightSeat;
    public FlightEntity flight;

    @InjectMocks
    public FlightTicketService flightTicketService;

    @BeforeEach
    void setUp() {
        // Preparing a flightTicket Mock
        seat = new SeatEntity();
        seat.setAircraft(new AircraftEntity());

        flightSeat = new FlightSeatEntity();
        flightSeat.setSeat(seat);

        flight = new FlightEntity();
        flight.setDepartureAirport(new AirportEntity());
        flight.setArrivalAirport(new AirportEntity());
        flight.setFlightSeats(List.of(flightSeat));

        flightTicketMock = new FlightTicketEntity();
        flightTicketMock.setFlight(flight);
        flightTicketMock.setSeat(seat);
        flightTicketMock.setPassenger(new PassengerEntity());
    }

    @Nested
    class listAllFlightTicketsTest {

        @Test
        @DisplayName("OK - Deve retornar uma página de tickets")
        void shouldReturnPageOfFlightTickets() {
            // Arrange
            var pageable = Pageable.unpaged();

            var flightTicketEntityPage = new PageImpl<>(List.of(flightTicketMock, flightTicketMock));

            when(flightTicketRepository.findAll(pageable)).thenReturn(flightTicketEntityPage);

            // Act
            Page<GetFlightTicketDTO> result = flightTicketService.listAllFlightTickets(pageable);

            // Assert
            verify(flightTicketRepository, times(1)).findAll(pageable);
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
        }

        @Test
        @DisplayName("OK - Deve retornar uma página vazia quando não houver tickets")
        void shouldReturnEmptyPageWhenThereIsNoTickets() {
            // Arrange
            Pageable pageable = Pageable.unpaged();

            when(flightTicketRepository.findAll(pageable)).thenReturn(Page.empty());

            // Act
            Page<GetFlightTicketDTO> result = flightTicketService.listAllFlightTickets(pageable);

            // Assert
            verify(flightTicketRepository, times(1)).findAll(pageable);
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
        }
    }

    @Nested
    class createFlightTicketTest {

        @Test
        @DisplayName("OK - Deve criar uma nova passagem")
        void shouldCreateFlightTicket() {
            // Arrange
            var dto = createPostFlightTicketDTO();

            flightSeat.setSeatAvailability(true);

            flight.setBasePrice(new BigDecimal("500"));
            flight.setAvailableSeatsAmount(1);
            flight.setStatus(FlightStatus.SCHEDULED);
            when(flightService.findFlightEntityById(dto.flightId())).thenReturn(flight);

            seat.setId(UUID.randomUUID());
            seat.setSeatClass(SeatTypeEnum.FIRST_CLASS);
            when(seatService.findSeatEntityById(dto.seatId())).thenReturn(seat);

            when(passengerService.findPassengerEntityById(dto.passengerId())).thenReturn(new PassengerEntity());

            // Act
            GetFlightTicketDTO result = flightTicketService.createFlightTicket(dto);

            // Assert
            verify(flightService, times(1)).findFlightEntityById(dto.flightId());
            verify(seatService, times(1)).findSeatEntityById(dto.seatId());
            verify(passengerService, times(1)).findPassengerEntityById(dto.passengerId());
            verify(flightTicketRepository, times(1)).save(any(FlightTicketEntity.class));

            assertThat(result).isNotNull();
            assertThat(result.totalPrice()).isEqualTo(new BigDecimal("650.0"));

            assertThat(result.ticketNumber()).isNotNull(); // Assert ticket number generation
            assertThat(flightSeat.isSeatAvailability()).isFalse(); // Assert that seat availability is changed
            assertThat(flight.getAvailableSeatsAmount()).isEqualTo(0); // Assert that the seats amount was updated
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção quando parâmetro dto for nulo")
        void shouldThrowExceptionWhenDtoIsNull() {
            // Act and Assert
            var ex = assertThrows(ValidationException.class, () -> flightTicketService.createFlightTicket(null));

            assertThat(ex).isInstanceOf(ValidationException.class)
                    .hasMessage("Informações do ticket não podem ser nulas!");

            verifyNoInteractions(flightTicketRepository);
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção para voo com status não agendado")
        void shouldThrowExceptionForNonScheduledFlight() {
            // Arrange
            var dto = createPostFlightTicketDTO();

            flight.setStatus(FlightStatus.BOARDING);
            when(flightService.findFlightEntityById(dto.flightId())).thenReturn(flight);

            // Act and Assert
            var ex = assertThrows(ValidationException.class, () -> flightTicketService.createFlightTicket(dto));

            assertThat(ex).isInstanceOf(ValidationException.class)
                    .hasMessage("Voo não pode ser agendado pois já é passada a data de um possível agendamento");

            verifyNoMoreInteractions(flightTicketRepository);
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção para voo sem qauntidade de assentos disponíveis")
        void shouldThrowExceptionForFlightWithoutAvailableSeats() {
            // Arrange
            var dto = createPostFlightTicketDTO();

            flight.setStatus(FlightStatus.SCHEDULED);
            flight.setAvailableSeatsAmount(0);
            when(flightService.findFlightEntityById(dto.flightId())).thenReturn(flight);

            // Act and Assert
            var ex = assertThrows(ValidationException.class, () -> flightTicketService.createFlightTicket(dto));

            assertThat(ex).isInstanceOf(ValidationException.class)
                    .hasMessage("Não há assento disponível para este voo");

            verifyNoMoreInteractions(flightTicketRepository);
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção para assento indisponível")
        void shouldThrowExceptionForUnavailableSeat() {
            // Arrange
            var dto = createPostFlightTicketDTO();

            flight.setBasePrice(new BigDecimal("500"));
            flightSeat.setSeatAvailability(false);
            flight.setAvailableSeatsAmount(1);
            flight.setStatus(FlightStatus.SCHEDULED);
            when(flightService.findFlightEntityById(dto.flightId())).thenReturn(flight);

            seat.setId(UUID.randomUUID());
            seat.setSeatClass(SeatTypeEnum.FIRST_CLASS);
            when(seatService.findSeatEntityById(dto.seatId())).thenReturn(seat);

            when(passengerService.findPassengerEntityById(dto.passengerId())).thenReturn(new PassengerEntity());

            // Act and Assert
            var ex = assertThrows(ValidationException.class, () -> flightTicketService.createFlightTicket(dto));
            assertThat(ex).isInstanceOf(ValidationException.class)
                    .hasMessage("Assento indisponível!");

            assertThat(flight.getAvailableSeatsAmount()).isEqualTo(1);
            verifyNoMoreInteractions(flightTicketRepository);
        }

        private PostFlightTicketDTO createPostFlightTicketDTO() {
            return new PostFlightTicketDTO(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        }
    }

    @Nested
    class findFlightTicketByIdTest {

        @Test
        @DisplayName("OK - Deve encontrar ticket pelo ID")
        void shouldFindFlightTicketById() {
            // Arrange
            var flightTicketId = UUID.randomUUID();
            flightTicketMock.setId(flightTicketId);
            when(flightTicketRepository.findById(flightTicketId)).thenReturn(Optional.of(flightTicketMock));

            // Act
            GetFlightTicketDTO result = flightTicketService.findFlightTicketById(flightTicketId);

            // Assert
            verify(flightTicketRepository, times(1)).findById(flightTicketId);
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(flightTicketId);
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção para passagem não encontrada")
        void shouldThrowExceptionForNonExistentFlightTicket() {
            // Arrange
            var flightTicketId = UUID.randomUUID();

            when(flightTicketRepository.findById(flightTicketId)).thenReturn(Optional.empty());

            // Act and Assert
            var ex = assertThrows(ResourceNotFoundException.class,
                    () -> flightTicketService.findFlightTicketById(flightTicketId));

            assertThat(ex).isInstanceOf(ResourceNotFoundException.class).hasMessage("Ticket não encontrado!");
            verify(flightTicketRepository, times(1)).findById(flightTicketId);
        }
    }

    @Nested
    class cancelTicketTest {

        @Test
        @DisplayName("OK - Deve cancelar um ticket atualizando o assento antes comprado")
        void shouldCancelATicketUpdatingTheSeatBeforeBought() {
            // Arrange
            var existentId = UUID.randomUUID();

            seat.setId(UUID.randomUUID());
            flight.setDepartureTime(LocalDateTime.now().minusHours(25));
            flight.setAvailableSeatsAmount(0);
            flightSeat.setSeatAvailability(false);
            flightTicketMock.setCanceled(false);

            when(flightTicketRepository.findById(existentId)).thenReturn(Optional.of(flightTicketMock));

            // Act
            flightTicketService.cancelTicket(existentId);

            // Assert
            verify(flightTicketRepository, times(1)).findById(existentId);
            verify(flightTicketRepository, times(1)).save(ticketCaptor.capture());

            assertThat(flightSeat.isSeatAvailability()).isTrue(); // assert seat is available
            assertThat(flight.getAvailableSeatsAmount()).isEqualTo(1);

            var flightTicketUpdated = ticketCaptor.getValue();
            assertThat(flightTicketUpdated.isCanceled()).isTrue();
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção quando ticket não for achado")
        void shouldThrowExceptionWhenTicketIsNotFound() {
            // Arrange
            var nonExistentId = UUID.randomUUID();

            when(flightTicketRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            // Act and Assert
            var ex = assertThrows(ResourceNotFoundException.class,
                    () -> flightTicketService.cancelTicket(nonExistentId));

            assertThat(ex).isInstanceOf(ResourceNotFoundException.class).hasMessage("Ticket não encontrado");
            verifyNoMoreInteractions(flightTicketRepository);
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção quando um ticket já estiver cancelado")
        void shouldThrowExceptionWhenTicketIsAlreadyCanceled() {
            // Arrange
            var existentId = UUID.randomUUID();

            flight.setAvailableSeatsAmount(0);
            flightSeat.setSeatAvailability(false);
            flightTicketMock.setCanceled(true);

            when(flightTicketRepository.findById(existentId)).thenReturn(Optional.of(flightTicketMock));

            // Act and Assert
            var ex = assertThrows(ValidationException.class,
                    () -> flightTicketService.cancelTicket(existentId));

            assertThat(ex).isInstanceOf(ValidationException.class)
                    .hasMessage("Este ticket já foi cancelado anteriormente");

            verify(flightTicketRepository, times(1)).findById(existentId);
            verifyNoMoreInteractions(flightTicketRepository);

            assertThat(flightSeat.isSeatAvailability()).isFalse(); // assert seat was not updated
            assertThat(flight.getAvailableSeatsAmount()).isEqualTo(0); // assert flight was not updated
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção quando um ticket for cancelado sem antecedêcia de 24 horas")
        void shouldThrowExceptionWhenTicketIsCanceledWithoutAntecedence() {
            // Arrange
            var existentId = UUID.randomUUID();

            flight.setDepartureTime(LocalDateTime.now().minusHours(23));
            flight.setAvailableSeatsAmount(0);
            flightSeat.setSeatAvailability(false);
            flightTicketMock.setCanceled(false);

            when(flightTicketRepository.findById(existentId)).thenReturn(Optional.of(flightTicketMock));

            // Act and Assert
            var ex = assertThrows(ValidationException.class,
                    () -> flightTicketService.cancelTicket(existentId));

            assertThat(ex).isInstanceOf(ValidationException.class)
                    .hasMessage("Cancelamento deve ser feito com pelo menos 24 horas de antecedência");

            verify(flightTicketRepository, times(1)).findById(existentId);
            verifyNoMoreInteractions(flightTicketRepository);

            assertThat(flightSeat.isSeatAvailability()).isFalse(); // assert seat was not updated
            assertThat(flight.getAvailableSeatsAmount()).isEqualTo(0); // assert flight was not updated
        }
    }

    @Nested
    class getTicketsByPassengerTest {

        @Test
        @DisplayName("OK - Deve retornar página de tickets por passageiro")
        void shouldReturnTicketPageByPassenger() {
            // Arrange
            var passengerId = UUID.randomUUID();
            var pageable = Pageable.unpaged();
            var tickets = List.of(flightTicketMock, flightTicketMock);

            Page<FlightTicketEntity> ticketPage = new PageImpl<>(tickets);

            when(flightTicketRepository.findByPassengerId(passengerId, pageable)).thenReturn(ticketPage);

            // Act
            Page<GetFlightTicketDTO> result = flightTicketService.getTicketsByPassenger(passengerId, pageable);

            // Assert
            verify(flightTicketRepository, times(1)).findByPassengerId(passengerId, pageable);
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(tickets.size());
        }

        @Test
        @DisplayName("OK - Deve retornar página vazia qaundo tickets não encontrados")
        void shouldReturnTicketEmptyPageWhenNothingFound() {
            // Arrange
            var passengerId = UUID.randomUUID();
            var pageable = Pageable.unpaged();

            when(flightTicketRepository.findByPassengerId(passengerId, pageable)).thenReturn(Page.empty());

            // Act
            Page<GetFlightTicketDTO> result = flightTicketService.getTicketsByPassenger(passengerId, pageable);

            // Assert
            verify(flightTicketRepository, times(1)).findByPassengerId(passengerId, pageable);
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
        }
    }
}