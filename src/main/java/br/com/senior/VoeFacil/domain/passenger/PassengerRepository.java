package br.com.senior.VoeFacil.domain.passenger;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PassengerRepository extends JpaRepository<PassengerEntity, UUID> {
}
