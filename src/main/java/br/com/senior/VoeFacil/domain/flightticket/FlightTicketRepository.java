package br.com.senior.VoeFacil.domain.flightticket;

import br.com.senior.VoeFacil.domain.flight.FlightEntity;
import br.com.senior.VoeFacil.domain.flightticket.DTO.GetFlightTicketDTO;
import br.com.senior.VoeFacil.domain.passenger.PassengerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FlightTicketRepository extends JpaRepository<FlightTicketEntity, UUID> {
    Page<FlightTicketEntity> findByPassengerId(UUID id, Pageable paging);
}
