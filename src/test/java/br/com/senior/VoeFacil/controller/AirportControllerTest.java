package br.com.senior.VoeFacil.controller;

import br.com.senior.VoeFacil.domain.airport.AirportService;
import br.com.senior.VoeFacil.domain.airport.DTO.GetAirportDTO;
import br.com.senior.VoeFacil.domain.airport.DTO.PostAirportDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.BDDMockito.given;

@WebMvcTest(AirportController.class)
public class AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AirportService airportService;

    @Test
    public void testListAllAirports() throws Exception {
        GetAirportDTO getAirportDTO = new GetAirportDTO(UUID.randomUUID(), "Airport Name", "Airport Code", "Country", "City");
        Page<GetAirportDTO> page = new PageImpl<>(Collections.singletonList(getAirportDTO));

        given(airportService.listAllAirports(PageRequest.of(0, 1))).willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/airport")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(getAirportDTO.id().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value(getAirportDTO.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].code").value(getAirportDTO.code()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].country").value(getAirportDTO.country()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].city").value(getAirportDTO.city()));
    }

    @Test
    public void testCreateAirport() throws Exception {
        PostAirportDTO postAirportDTO = new PostAirportDTO("Airport Name", "Airport Code", "Country", "City");
        GetAirportDTO getAirportDTO = new GetAirportDTO(UUID.randomUUID(), "Airport Name", "Airport Code", "Country", "City");

        given(airportService.createAirport(postAirportDTO)).willReturn(getAirportDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/airport")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postAirportDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(getAirportDTO.id().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(getAirportDTO.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(getAirportDTO.code()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value(getAirportDTO.country()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(getAirportDTO.city()));
    }

    @Test
    public void testGetAirportById() throws Exception {
        UUID airportId = UUID.randomUUID();
        GetAirportDTO getAirportDTO = new GetAirportDTO(airportId, "Airport Name", "Airport Code", "Country", "City");

        given(airportService.findAirportById(airportId)).willReturn(getAirportDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/airport/" + airportId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(getAirportDTO.id().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(getAirportDTO.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(getAirportDTO.code()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value(getAirportDTO.country()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(getAirportDTO.city()));
    }
}

