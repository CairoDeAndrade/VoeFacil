package br.com.senior.VoeFacil.domain.passenger.DTO;

import br.com.senior.VoeFacil.domain.passenger.PassengerEntity;

import java.time.LocalDateTime;

public record GetPassengerDTO(Long id, String name, String email, String phone, LocalDateTime birthDate) {
    public GetPassengerDTO(PassengerEntity entity) {
        this(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone(), entity.getBirthDate());
    }
}
