package br.com.senior.VoeFacil.domain.airport;

import br.com.senior.VoeFacil.domain.airport.DTO.GetAirportDTO;
import br.com.senior.VoeFacil.domain.airport.DTO.PostAirportDTO;
import br.com.senior.VoeFacil.infra.exception.ResourceNotFoundException;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AirportService {

    @Autowired
    private AirportRepository repository;

    @Transactional(readOnly = true)
    public Page<GetAirportDTO> listAllAirports(Pageable paging) {
        return repository.findAll(paging).map(GetAirportDTO::new);
    }

    @Transactional
    public GetAirportDTO createAirport(PostAirportDTO dto) {
        if (dto == null) {
            throw new ValidationException("Dados do aeroporto não podem ser nulos!");
        }

        var airport = new AirportEntity(dto);
        repository.save(airport);
        return new GetAirportDTO(airport);
    }

    @Transactional(readOnly = true)
    public GetAirportDTO findAirportById(UUID id){
        var airport = findEntityById(id);
        return new GetAirportDTO(airport);
    }

    @Transactional(readOnly = true)
    public AirportEntity findEntityById(UUID uuid) {
        return repository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Aeroporto não encontrado!"));
    }
}
