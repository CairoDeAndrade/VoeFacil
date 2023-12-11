package br.com.senior.VoeFacil.domain.passenger.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PostPassengerDTO(
        @NotBlank
        String name,
        String email,
        String phone,
        @NotNull
        LocalDateTime birthDate) {
}
