package br.com.senior.VoeFacil.domain.passenger.DTO;

import br.com.senior.VoeFacil.domain.passenger.PassengerEntity;

import java.util.UUID;

public record GetPassengerDTO(UUID id, String firstName, String lastName, String email, String phone) {
    public GetPassengerDTO(PassengerEntity entity) {
        this(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getPhone());
    }
}
