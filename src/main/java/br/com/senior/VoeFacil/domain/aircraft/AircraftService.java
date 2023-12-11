package br.com.senior.VoeFacil.domain.aircraft;

import br.com.senior.VoeFacil.domain.aircraft.DTO.GetAircraftDTO;
import br.com.senior.VoeFacil.domain.aircraft.DTO.PostAircraftDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AircraftService {

    @Autowired
    private AircraftRepository aircraftRepository;

    public Page<GetAircraftDTO> listAllAircraft(Pageable paging) {
        return aircraftRepository.findAll(paging).map(GetAircraftDTO::new);
    }

    @Transactional
    public GetAircraftDTO createAircraft(PostAircraftDTO dto) {
        var aircraft = new AircraftEntity(dto);
        aircraftRepository.save(aircraft);
        return new GetAircraftDTO(aircraft);
    }

    public GetAircraftDTO findAircraftById(Long id){
        var aircraft = aircraftRepository.getReferenceById(id);
        return new GetAircraftDTO(aircraft);
    }
}
