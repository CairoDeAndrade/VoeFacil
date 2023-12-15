package br.com.senior.VoeFacil.domain.flight.validations.insert;

import br.com.senior.VoeFacil.domain.flight.DTO.PostFlightDTO;
import br.com.senior.VoeFacil.infra.exception.ValidationException;

import java.time.Duration;
import java.time.LocalDate;

public class ValidateFlightDepartureAntecedence implements InsertFlightValidator {

    @Override
    public void validate(PostFlightDTO dto) {
        LocalDate today = LocalDate.now();
        LocalDate departureFlightDate = dto.departureTime().toLocalDate();
        long difference = Duration.between(departureFlightDate, today).toDays();

        if (difference < 7L) {
            throw new ValidationException("Data de partida do voo deve ser no mínimo 7 dias antes da data atual");
        }
    }
}