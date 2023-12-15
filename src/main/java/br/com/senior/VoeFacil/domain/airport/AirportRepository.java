package br.com.senior.VoeFacil.domain.airport;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirportRepository extends JpaRepository<AirportEntity, UUID> {
}
