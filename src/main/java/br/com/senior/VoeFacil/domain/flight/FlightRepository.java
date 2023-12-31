package br.com.senior.VoeFacil.domain.flight;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.UUID;

public interface FlightRepository extends JpaRepository<FlightEntity, UUID>, JpaSpecificationExecutor<FlightEntity> {

    @Query(nativeQuery = true, value = """ 
            SELECT
            	count(f.id) > 0
            FROM Flight f
            INNER JOIN flight_has_seat fhs ON fhs.flight_id = f.id
            INNER JOIN seat s ON s.id = fhs.seat_id
            INNER JOIN aircraft ac ON ac.id = s.aircraft_id
            WHERE f.departure_time = :departureTime
            	AND f.status != 'CANCELED'
            	AND s.aircraft_id = :aircraftId
            """)
    boolean existsFlightByDepartureTimeAndAircraft(LocalDateTime departureTime, UUID aircraftId);

    Page<FlightEntity> findByDeal(boolean deal, Pageable pageable);
}
