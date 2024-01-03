package br.com.senior.VoeFacil.controller;

import br.com.senior.VoeFacil.domain.passenger.DTO.GetPassengerDTO;
import br.com.senior.VoeFacil.domain.passenger.DTO.PostPassengerDTO;
import br.com.senior.VoeFacil.domain.passenger.PassengerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.BDDMockito.given;

@WebMvcTest(PassengerController.class)
public class PassengerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PassengerService passengerService;

    @Test
    public void testCreatePassenger() throws Exception {
        // Arrange
        PostPassengerDTO postPassengerDTO = new PostPassengerDTO("William", "Nogueira", "william.nogueira@gmail.com", "(47) 98954-5784)");
        GetPassengerDTO getPassengerDTO = new GetPassengerDTO(UUID.randomUUID(), "William", "Nogueira", "william.nogueira@gmail.com", "(47) 98954-5784)");

        // Act
        given(passengerService.createPassenger(postPassengerDTO)).willReturn(getPassengerDTO);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postPassengerDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(getPassengerDTO.id().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(getPassengerDTO.firstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(getPassengerDTO.lastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(getPassengerDTO.email()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(getPassengerDTO.phone()));
    }

    @Test
    public void testGetPassengerById() throws Exception {
        // Arrange
        UUID passengerId = UUID.randomUUID();
        GetPassengerDTO getPassengerDTO = new GetPassengerDTO(passengerId, "Cairo", "Augusto", "cairo.augusto@gmail.com", "(47) 98812-5412");

        // Act
        given(passengerService.findPassengerById(passengerId)).willReturn(getPassengerDTO);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/passenger/" + passengerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(getPassengerDTO.id().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(getPassengerDTO.firstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(getPassengerDTO.lastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(getPassengerDTO.email()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(getPassengerDTO.phone()));
    }
}

