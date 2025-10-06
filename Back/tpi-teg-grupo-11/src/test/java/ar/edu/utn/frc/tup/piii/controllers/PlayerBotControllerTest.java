package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.PlayerDto;
import ar.edu.utn.frc.tup.piii.services.interfaces.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayerBotController.class)
class PlayerBotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "botPlayerServiceImpl")
    private PlayerService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_returnsListOfPlayers() throws Exception {
        PlayerDto dto = new PlayerDto();
        dto.setPlayerId(1L);

        Mockito.when(service.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/bot"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].playerId", is(1)));
    }

    @Test
    void getByGame_returnsPlayersForGame() throws Exception {
        UUID gameId = UUID.randomUUID();
        PlayerDto dto = new PlayerDto();
        dto.setPlayerId(2L);

        Mockito.when(service.getPlayersByGame(gameId)).thenReturn(List.of(dto));

        mockMvc.perform(get("/bot/game/{gameId}", gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].playerId", is(2)));
    }

    @Test
    void createPlayer_validRequest_returnsCreatedPlayer() throws Exception {
        PlayerDto inputDto = new PlayerDto();
        PlayerDto savedDto = new PlayerDto();
        savedDto.setPlayerId(3L);

        Mockito.when(service.createPlayer(any(PlayerDto.class))).thenReturn(savedDto);

        mockMvc.perform(post("/bot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerId", is(3)));
    }
}
