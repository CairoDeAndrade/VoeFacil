package br.com.senior.VoeFacil.domain.passenger.DTO;

import br.com.senior.VoeFacil.domain.passenger.PassengerEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetPassengerDTO(
        UUID id,
        String name,
        String email,
        String phone,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDate birthDate) {
    public GetPassengerDTO(PassengerEntity entity) {
        this(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone(), entity.getBirthDate());
    }
}
