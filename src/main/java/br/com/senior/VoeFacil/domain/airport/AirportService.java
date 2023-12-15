package br.com.senior.VoeFacil.domain.airport;

import br.com.senior.VoeFacil.domain.aircraft.DTO.GetAircraftDTO;
import br.com.senior.VoeFacil.domain.airport.DTO.GetAirportDTO;
import br.com.senior.VoeFacil.domain.airport.DTO.PostAirportDTO;
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
        var airport = new AirportEntity(dto);
        repository.save(airport);
        return new GetAirportDTO(airport);
    }

    @Transactional(readOnly = true)
    public GetAirportDTO findAirportById(UUID id){
        var airport = repository.getReferenceById(id);
        return new GetAirportDTO(airport);
    }

}
