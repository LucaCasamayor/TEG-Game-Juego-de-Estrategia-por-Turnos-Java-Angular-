package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.MovementDto;
import ar.edu.utn.frc.tup.piii.services.MovementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovementController.class)
class MovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovementService service;

    @Autowired
    private ObjectMapper objectMapper;

    private MovementDto movementDto;

    @BeforeEach
    void setUp() {
        movementDto = new MovementDto();
        movementDto.setId(1L);
        movementDto.setArmyCount(5);
    }

    @Test
    void getMovementById_ShouldReturnOk() throws Exception {
        when(service.getMovementById(1L)).thenReturn(movementDto);

        mockMvc.perform(get("/movements/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getMovementById_ShouldReturnNotFound() throws Exception {
        when(service.getMovementById(1L)).thenReturn(null);

        mockMvc.perform(get("/movements/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllMovementsByGame_ShouldReturnList() throws Exception {
        String gameId = UUID.randomUUID().toString();
        when(service.getAllByGame(gameId)).thenReturn(List.of(movementDto));

        mockMvc.perform(get("/movements/game/" + gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void saveMovement_ShouldReturnOk() throws Exception {
        when(service.saveMovement(any())).thenReturn(movementDto);

        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movementDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void saveMovement_ShouldReturnServerError() throws Exception {
        when(service.saveMovement(any())).thenReturn(null);

        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movementDto)))
                .andExpect(status().isInternalServerError());
    }
}
