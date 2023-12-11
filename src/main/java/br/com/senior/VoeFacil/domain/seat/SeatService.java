package br.com.senior.VoeFacil.domain.seat;

import br.com.senior.VoeFacil.domain.aircraft.AircraftRepository;
import br.com.senior.VoeFacil.domain.seat.DTO.GetSeatDTO;
import br.com.senior.VoeFacil.domain.seat.DTO.PostSeatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    public Page<GetSeatDTO> listAllSeats(Pageable pageable){
        return seatRepository.findAll(pageable).map(GetSeatDTO::new);
    }

    @Transactional
    public GetSeatDTO createSeat(PostSeatDTO dto){
        var aircraft = aircraftRepository.findById(dto.aircraft_id())
                .orElseThrow(IllegalArgumentException::new);

        var seat = new SeatEntity(dto.seatNumber(), dto.seatClass(), aircraft);
        seatRepository.save(seat);
        return new GetSeatDTO(seat);
    }

    public GetSeatDTO findSeatById(Long id){
        var seat = seatRepository.getReferenceById(id);
        return new GetSeatDTO(seat);
    }

}
