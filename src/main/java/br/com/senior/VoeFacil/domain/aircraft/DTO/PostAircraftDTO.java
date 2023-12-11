package br.com.senior.VoeFacil.domain.aircraft.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostAircraftDTO(
        @NotBlank
        String airline,
        @NotNull
        int capacity) {
}
