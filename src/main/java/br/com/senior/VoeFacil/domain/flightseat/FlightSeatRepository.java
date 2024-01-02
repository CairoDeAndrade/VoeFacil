package br.com.senior.VoeFacil.domain.flightseat;

import br.com.senior.VoeFacil.domain.flightseat.DTO.GetFlightSeatDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FlightSeatRepository extends JpaRepository<FlightSeatEntity, UUID> {

    @Query("""
                SELECT new br.com.senior.VoeFacil.domain.flightseat.DTO.GetFlightSeatDTO(fse.seat.id, fse.seatAvailability, fse.seat.seatNumber, fse.seat.seatClass)
                FROM FlightSeat fse
                WHERE fse.flight.id = :flightId
                ORDER BY fse.seat.seatNumber ASC
            """)
    List<GetFlightSeatDTO> findAllByFlightId(UUID flightId);

}
