package br.com.senior.VoeFacil.domain.flight.validations.insert;

import br.com.senior.VoeFacil.domain.flight.DTO.PostFlightDTO;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class ValidateFlightDepartureAntecedence implements InsertFlightValidator {

    @Override
    public void validate(PostFlightDTO dto) {
        LocalDate today = LocalDate.now();
        LocalDate departureFlightDate = dto.departureTime().toLocalDate();
        long difference = ChronoUnit.DAYS.between(today, departureFlightDate);

        if (difference < 7L) {
            throw new ValidationException("Data de partida do voo deve ser no mínimo 7 dias antes da data atual");
        }
    }
}
