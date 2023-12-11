package br.com.senior.VoeFacil.domain.flight;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<FlightEntity, Long> {
}
