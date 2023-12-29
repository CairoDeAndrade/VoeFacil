package br.com.senior.VoeFacil.domain.seat;

import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.aircraft.AircraftRepository;
import br.com.senior.VoeFacil.domain.seat.DTO.GetSeatDTO;
import br.com.senior.VoeFacil.domain.seat.DTO.PostSeatDTO;
import br.com.senior.VoeFacil.infra.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Transactional(readOnly = true)
    public Page<GetSeatDTO> listAllSeats(Pageable pageable){
        return seatRepository.findAll(pageable).map(GetSeatDTO::new);
    }

    @Transactional
    public GetSeatDTO createSeat(PostSeatDTO dto){
        var aircraft = aircraftRepository.findById(dto.aircraft_id())
                .orElseThrow(() -> new ResourceNotFoundException("Aeronave não encontrada"));

        var seat = new SeatEntity(dto.seatNumber(), dto.seatClass(), aircraft);
        seatRepository.save(seat);

        return new GetSeatDTO(seat);
    }

    @Transactional(readOnly = true)
    public GetSeatDTO findSeatById(UUID id){
        var seat = findSeatEntityById(id);
        return new GetSeatDTO(seat);
    }

    @Transactional(readOnly = true)
    public List<SeatEntity> findAllEntitiesByAircraft(AircraftEntity aircraft) {
        return seatRepository.findAllByAircraft(aircraft);
    }

    @Transactional(readOnly = true)
    public SeatEntity findSeatEntityById(UUID id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assento não encontrado!"));
    }
}
