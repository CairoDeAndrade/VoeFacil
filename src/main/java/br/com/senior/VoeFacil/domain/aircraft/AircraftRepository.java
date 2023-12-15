package br.com.senior.VoeFacil.domain.aircraft;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AircraftRepository extends JpaRepository<AircraftEntity, UUID> {
}
