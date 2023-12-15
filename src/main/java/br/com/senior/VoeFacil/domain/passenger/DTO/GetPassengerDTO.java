package br.com.senior.VoeFacil.domain.passenger.DTO;

import br.com.senior.VoeFacil.domain.passenger.PassengerEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetPassengerDTO(UUID id, String name, String email, String phone, LocalDate birthDate) {
    public GetPassengerDTO(PassengerEntity entity) {
        this(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone(), entity.getBirthDate());
    }
}
