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

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "humanPlayerServiceImpl")
    private PlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_returnsListOfPlayers() throws Exception {
        PlayerDto dto = new PlayerDto();
        dto.setPlayerId(1L);

        Mockito.when(playerService.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/player"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].playerId", is(1)));
    }

    @Test
    void getByGame_returnsPlayersForGame() throws Exception {
        UUID gameId = UUID.randomUUID();
        PlayerDto dto = new PlayerDto();
        dto.setPlayerId(2L);

        Mockito.when(playerService.getPlayersByGame(gameId)).thenReturn(List.of(dto));

        mockMvc.perform(get("/player/game/{gameId}", gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].playerId", is(2)));
    }

    @Test
    void createPlayer_validRequest_returnsCreatedPlayer() throws Exception {
        PlayerDto inputDto = new PlayerDto();
        PlayerDto savedDto = new PlayerDto();
        savedDto.setPlayerId(3L);

        Mockito.when(playerService.createPlayer(any(PlayerDto.class))).thenReturn(savedDto);

        mockMvc.perform(post("/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.playerId", is(3)));
    }
}
