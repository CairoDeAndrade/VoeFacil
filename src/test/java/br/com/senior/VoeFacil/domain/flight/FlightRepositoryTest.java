package br.com.senior.VoeFacil.domain.flight;

import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.airport.AirportEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class FlightRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    FlightRepository repository;

    @Nested
    class existsFlightByDepartureTimeAndAircraftTest {

        @Test
        @DisplayName("OK - Deve trazer true quando uma aeronave já possui um voo naquela data")
        void shouldReturnTrueWhenAircraftAlreadyHaveAnotherFlightInThePassedDate() {

        }

        private AircraftEntity createAircraft() {return  null;}
        private AirportEntity createAirport() {return  null;}
    }
}