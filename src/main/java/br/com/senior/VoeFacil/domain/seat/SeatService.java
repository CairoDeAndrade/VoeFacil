package br.com.senior.VoeFacil.domain.seat;

import br.com.senior.VoeFacil.domain.aircraft.AircraftEntity;
import br.com.senior.VoeFacil.domain.aircraft.AircraftRepository;
import br.com.senior.VoeFacil.domain.seat.DTO.GetSeatDTO;
import br.com.senior.VoeFacil.domain.seat.DTO.PostSeatDTO;
import br.com.senior.VoeFacil.infra.exception.ResourceNotFoundException;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public GetSeatDTO createSeat(PostSeatDTO dto){
        if (dto == null) {
            throw new ValidationException("Dados da cadeira não podem ser nulos!");
        }

        var aircraft = aircraftRepository.findById(dto.aircraft_id())
                .orElseThrow(() -> new ResourceNotFoundException("Aeronave não encontrada"));

        var seat = new SeatEntity(dto.seatNumber(), dto.seatClass(), aircraft);
        seatRepository.save(seat);

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
