package br.com.senior.VoeFacil.domain.passenger;

import br.com.senior.VoeFacil.domain.passenger.DTO.GetPassengerDTO;
import br.com.senior.VoeFacil.domain.passenger.DTO.PostPassengerDTO;
import br.com.senior.VoeFacil.infra.exception.ResourceNotFoundException;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Transactional
    public GetPassengerDTO createPassenger(PostPassengerDTO dto){
        if (dto == null) {
            throw new ValidationException("Dados do passageiro não podem ser nulos!");
        }

        var passenger = new PassengerEntity(dto);
        passengerRepository.save(passenger);
        return new GetPassengerDTO(passenger);
    }

    @Transactional(readOnly = true)
    public GetPassengerDTO findPassengerById(UUID id){
        var passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passageiro não encontrado!"));
        return new GetPassengerDTO(passenger);
    }

    @Transactional(readOnly = true)
    public PassengerEntity findPassengerEntityById(UUID id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passageiro não encontrado!"));
    }
}
