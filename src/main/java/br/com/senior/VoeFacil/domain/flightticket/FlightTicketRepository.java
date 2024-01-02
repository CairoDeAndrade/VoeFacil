package br.com.senior.VoeFacil.domain.flightticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FlightTicketRepository extends JpaRepository<FlightTicketEntity, UUID> {
    Page<FlightTicketEntity> findByPassengerId(UUID id, Pageable paging);
}
