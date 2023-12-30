package br.com.senior.VoeFacil.domain.flightseat;

import br.com.senior.VoeFacil.domain.flightseat.DTO.GetFlightSeatDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FlightSeatRepository extends JpaRepository<FlightSeatEntity, UUID> {

    @Query(nativeQuery = true, value = """
            SELECT fhs.seat_id, fhs.seat_availability, s.seat_number, s.seat_class
            FROM flight_has_seat fhs
            JOIN seat s ON fhs.seat_id = s.id
            WHERE fhs.flight_id = :flightId
            """
    )
    List<FlightSeatEntity> findAllByFlightId(UUID flightId);

}
