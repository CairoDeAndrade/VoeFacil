package br.com.senior.VoeFacil.domain.aircraft;

import br.com.senior.VoeFacil.domain.aircraft.DTO.GetAircraftDTO;
import br.com.senior.VoeFacil.domain.aircraft.DTO.PostAircraftDTO;
import br.com.senior.VoeFacil.domain.seat.SeatEntity;
import br.com.senior.VoeFacil.domain.seat.SeatRepository;
import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;
import br.com.senior.VoeFacil.infra.exception.ResourceNotFoundException;
import br.com.senior.VoeFacil.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class AircraftService {

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Transactional
    public GetAircraftDTO createAircraft(PostAircraftDTO dto) {
        if (dto == null) {
            throw new ValidationException("Dados da aeronave não podem ser nulos!");
        }

        var aircraft = new AircraftEntity(dto);
        int numberOfSeats = aircraft.getCapacity();

        // Create all aircraft seats
        List<SeatEntity> seats = IntStream.range(1, numberOfSeats + 1)
                .mapToObj(i -> {
                    SeatEntity seat = new SeatEntity();
                    seat.setSeatNumber(i);
                    seat.setSeatClass(i <= 20 ? SeatTypeEnum.FIRST_CLASS : SeatTypeEnum.ECONOMY);
                    seat.setAircraft(aircraft);
                    return seat;
                }).toList();

        seatRepository.saveAll(seats);
        aircraftRepository.save(aircraft);

        return new GetAircraftDTO(aircraft);
    }

    @Transactional(readOnly = true)
    public GetAircraftDTO findAircraftById(UUID id){
        var aircraft = findAircraftEntityById(id);
        return new GetAircraftDTO(aircraft);
    }

    @Transactional(readOnly = true)
    public AircraftEntity findAircraftEntityById(UUID id) {
        return aircraftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aeronave não encontrada!"));
    }
}
