package br.com.senior.VoeFacil.domain.passenger;

import br.com.senior.VoeFacil.domain.passenger.DTO.GetPassengerDTO;
import br.com.senior.VoeFacil.domain.passenger.DTO.PostPassengerDTO;
import br.com.senior.VoeFacil.infra.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Transactional(readOnly = true)
    public Page<GetPassengerDTO> listAllPassengers(Pageable pageable){
        return passengerRepository.findAll(pageable).map(GetPassengerDTO::new);
    }

    @Transactional
    public GetPassengerDTO createPassenger(PostPassengerDTO dto){
        var passenger = new PassengerEntity(dto);
        passengerRepository.save(passenger);
        return new GetPassengerDTO(passenger);
    }

    @Transactional(readOnly = true)
    public GetPassengerDTO findPassengerById(UUID id){
        var passenger = passengerRepository.getReferenceById(id);
        return new GetPassengerDTO(passenger);
    }

    @Transactional(readOnly = true)
    public PassengerEntity findPassengerEntityById(UUID id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passageiro não encontrado!"));
    }
}
