package br.com.senior.VoeFacil.controller;

import br.com.senior.VoeFacil.domain.seat.DTO.GetSeatDTO;
import br.com.senior.VoeFacil.domain.seat.DTO.PostSeatDTO;
import br.com.senior.VoeFacil.domain.seat.SeatService;
import br.com.senior.VoeFacil.domain.flightseat.FlightSeatService;
import br.com.senior.VoeFacil.domain.flightseat.DTO.GetFlightSeatDTO;
import br.com.senior.VoeFacil.domain.seat.SeatTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(SeatController.class)
public class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SeatService seatService;

    @MockBean
    private FlightSeatService flightSeatService;

    @Test
    public void testCreateSeat() throws Exception {
        // Arrange
        PostSeatDTO postSeatDTO = new PostSeatDTO(1, SeatTypeEnum.ECONOMY, UUID.randomUUID());
        GetSeatDTO getSeatDTO = new GetSeatDTO(UUID.randomUUID(), 1, SeatTypeEnum.ECONOMY, null);

        // Act
        given(seatService.createSeat(postSeatDTO)).willReturn(getSeatDTO);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/seat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postSeatDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(getSeatDTO.id().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seatNumber").value(getSeatDTO.seatNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seatClass").value(getSeatDTO.seatClass().toString()));
    }

    @Test
    public void testGetAllSeatsForFlight() throws Exception {
        // Arrange
        UUID flightId = UUID.randomUUID();
        GetFlightSeatDTO getFlightSeatDTO = new GetFlightSeatDTO(UUID.randomUUID(), true, 12, SeatTypeEnum.ECONOMY);

        // Act
        given(flightSeatService.getAllSeatsForFlight(flightId)).willReturn(List.of(getFlightSeatDTO));

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/seat/" + flightId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].seatId").value(getFlightSeatDTO.seatId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].seatAvailability").value(getFlightSeatDTO.seatAvailability()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].seatNumber").value(getFlightSeatDTO.seatNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].seatClass").value(getFlightSeatDTO.seatClass().toString()))
                .andDo(print());
    }
}