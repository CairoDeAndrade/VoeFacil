package br.com.senior.VoeFacil.domain.airport.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostAirportDTO(
        @NotBlank
        String name,
        @NotBlank
        String code,
        @NotBlank
        String country,
        @NotBlank
        String city) {
}
