package br.com.senior.VoeFacil.domain.aircraft.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PostAircraftDTO(
        @NotBlank
        String airline,
        @NotNull
        @Positive
        int capacity) {
}
