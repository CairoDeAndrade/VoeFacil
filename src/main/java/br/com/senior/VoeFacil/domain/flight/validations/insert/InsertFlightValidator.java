package br.com.senior.VoeFacil.domain.flight.validations.insert;

import br.com.senior.VoeFacil.domain.flight.DTO.PostFlightDTO;

public interface InsertFlightValidator {

    void validate(PostFlightDTO dto);
}
