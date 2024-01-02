package br.com.senior.VoeFacil.domain.airport;

import br.com.senior.VoeFacil.domain.airport.DTO.GetAirportDTO;
import br.com.senior.VoeFacil.domain.airport.DTO.PostAirportDTO;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportServiceTest {

    @Mock
    public AirportRepository repository;

    @Captor
    public ArgumentCaptor<AirportEntity> airportCaptor;

    @InjectMocks
    public AirportService service;

    public AirportEntity airport;

    @BeforeEach
    void setUp() {
        airport = new AirportEntity();
    }

    @Nested
    class listAllAirportsTest {

        @Test
        @DisplayName("OK - Deve listar os todos aeroportos")
        void shouldListAllAirports() {
            // Arrange
            var pageable = Pageable.unpaged();
            var airportsList = List.of(airport, airport);

            when(repository.findAll(pageable)).thenReturn(new PageImpl<>(airportsList));

            // Act
            Page<GetAirportDTO> result = service.listAllAirports(pageable);

            // Assert
            verify(repository, times(1)).findAll(pageable);
            assertThat(result).isNotNull();
            assertThat(result.getSize()).isEqualTo(airportsList.size());
        }

        @Test
        @DisplayName("OK - Deve retornar uam página vazia quando não houver aeroportos")
        void shouldReturnEmptyPageWhenAnyAirports() {
            // Arrange
            var pageable = Pageable.unpaged();
            when(repository.findAll(pageable)).thenReturn(Page.empty());

            // Act
            Page<GetAirportDTO> result = service.listAllAirports(pageable);

            // Assert
            verify(repository, times(1)).findAll(pageable);
            assertThat(result).isEmpty();
            assertThat(result.getSize()).isEqualTo(0);
        }
    }

    @Nested
    class createAirportTest {

        @Test
        @DisplayName("OK - Deve criar um novo aeroporto")
        void shouldCreateANewAirport() {
            // Arrange
            var dto = new PostAirportDTO(
                    "Viracopos",
                    "VRC",
                    "Brasil",
                    "São Paulo"
            );

            // Act
            GetAirportDTO result = service.createAirport(dto);

            // Assert
            verify(repository, times(1)).save(airportCaptor.capture());

            var capturedAirport = airportCaptor.getValue();
            assertThat(capturedAirport.getName()).isEqualTo(result.name());
            assertThat(capturedAirport.getCode()).isEqualTo(result.code());
            assertThat(capturedAirport.getCountry()).isEqualTo(result.country());
            assertThat(capturedAirport.getCity()).isEqualTo(result.city());
        }

        @Test
        @DisplayName("NOK - Deve lançar ecxeção quando parâmetro dto for nulo")
        void shouldThrowExceptionWhenParameterIsNull() {
            // Act and Assert
            var ex = assertThrows(ValidationException.class, () -> service.createAirport(null));
            assertThat(ex).isInstanceOf(ValidationException.class)
                    .hasMessage("Dados do aeroporto não podem ser nulos!");

            verifyNoInteractions(repository);
        }
    }

    @Nested
    class findAirportByIdTest {
        @Test
        @DisplayName("OK - Deve retornar uma aeronave pelo ID")
        void shouldReturnAircraftById() {
            // Arrange
            var airportId = UUID.randomUUID();
            airport.setId(airportId);

            when(repository.findById(airportId)).thenReturn(Optional.of(airport));

            // Act
            GetAirportDTO result = service.findAirportById(airportId);

            // Assert
            verify(repository, times(1)).findById(airportId);
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(airport.getId());
        }

        @Test
        @DisplayName("NOK - Deve lançar exceção ao não encontrar aeronave pelo ID")
        void shouldThrowExceptionWhenAircraftNotFoundById() {
            // Arrange
            UUID nonExistentId = UUID.randomUUID();

            when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

            // Act and Assert
            var ex = assertThrows(ResourceNotFoundException.class,
                    () -> service.findAirportById(nonExistentId));

            assertThat(ex).isInstanceOf(ResourceNotFoundException.class).hasMessage("Aeroporto não encontrado!");
        }
    }
}