package br.com.senior.VoeFacil.domain.aircraft;

import br.com.senior.VoeFacil.domain.aircraft.DTO.GetAircraftDTO;
import br.com.senior.VoeFacil.domain.aircraft.DTO.PostAircraftDTO;
import br.com.senior.VoeFacil.domain.seat.SeatEntity;
import br.com.senior.VoeFacil.domain.seat.SeatRepository;
import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;
import br.com.senior.VoeFacil.infra.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AircraftService {

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Transactional(readOnly = true)
    public Page<GetAircraftDTO> listAllAircraft(Pageable paging) {
        return aircraftRepository.findAll(paging).map(GetAircraftDTO::new);
    }

    @Transactional
    public GetAircraftDTO createAircraft(PostAircraftDTO dto) {
        var aircraft = new AircraftEntity(dto);

        int numberOfSeats = aircraft.getCapacity();

        // Cria cadeiras para o avião automaticamente
        for (int i = 0; i < numberOfSeats; i++) {
            SeatEntity seat = new SeatEntity();
            seat.setSeatNumber(i + 1);

            if (i < 20) {
                seat.setSeatClass(SeatTypeEnum.FIRST_CLASS);
            } else {
                seat.setSeatClass(SeatTypeEnum.ECONOMY);
            }

            seat.setAircraft(aircraft);
            seatRepository.save(seat);
        }

        aircraftRepository.save(aircraft);
        return new GetAircraftDTO(aircraft);
    }

    @Transactional(readOnly = true)
    public GetAircraftDTO findAircraftById(UUID id){
        var aircraft = aircraftRepository.getReferenceById(id);
        return new GetAircraftDTO(aircraft);
    }

    @Transactional(readOnly = true)
    public AircraftEntity findEntityById(UUID uuid) {
        return aircraftRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Aeroporto não encontrado!"));
    }
}
