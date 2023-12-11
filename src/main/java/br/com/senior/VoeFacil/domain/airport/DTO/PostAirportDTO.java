package br.com.senior.VoeFacil.domain.airport.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostAirportDTO(
        @NotBlank
        String name,
        @NotBlank
        String cep,
        @NotBlank
        String country,
        @NotBlank
        String city,
        @NotBlank
        String neighborhood,
        @NotBlank
        String street,
        @NotNull
        int streetNumber) {

}
