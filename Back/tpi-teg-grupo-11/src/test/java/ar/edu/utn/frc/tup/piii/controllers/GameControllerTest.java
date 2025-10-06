package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.*;
import ar.edu.utn.frc.tup.piii.enums.GameState;
import ar.edu.utn.frc.tup.piii.services.GamePersistenceService;
import ar.edu.utn.frc.tup.piii.services.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private GamePersistenceService gamePersistenceService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID gameId;
    private UUID userId;
    private GameDto gameDto;
    private GameDataDto gameDataDto;

    @BeforeEach
    void setUp() {
        gameId = UUID.randomUUID();
        userId = UUID.randomUUID();
        gameDto = new GameDto();
        gameDto.setGameId(gameId);

        gameDataDto = new GameDataDto();
        gameDataDto.setGameId(gameId);
    }
    @Test
    void loadGameData_ShouldReturnNotFound_WhenGameIsNull() throws Exception {
        when(gameService.findGameData(gameId)).thenReturn(null);

        mockMvc.perform(get("/games/" + gameId))
                .andExpect(status().isNotFound());
    }
    @Test
    void loadGameData_NotFound() throws Exception {
        when(gameService.findGameData(gameId)).thenReturn(null);

        mockMvc.perform(get("/games/" + gameId))
                .andExpect(status().isNotFound());
    }
    @Test
    void updateGameState_BadRequest() throws Exception {
        StatusDto statusDto = new StatusDto();
        statusDto.setGameState(GameState.IN_PROGRESS);
        when(gameService.updateGameState(any(), any())).thenReturn(null);

        mockMvc.perform(put("/games/state/" + gameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDto)))
                .andExpect(status().isBadRequest());
    }




    @Test
    void loadGameData_ShouldReturnOk() throws Exception {
        when(gameService.findGameData(gameId)).thenReturn(gameDataDto);

        mockMvc.perform(get("/games/" + gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(gameId.toString())));
    }

    @Test
    void loadGame_ShouldReturnOk() throws Exception {
        when(gameService.findGame(gameId)).thenReturn(gameDto);

        mockMvc.perform(get("/games/lobby/" + gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(gameId.toString())));
    }

    @Test
    void createGame_ShouldReturnCreated() throws Exception {
        when(gameService.createGame(any())).thenReturn(gameDto);

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void updateGameState_ShouldReturnOk() throws Exception {
        StatusDto statusDto = new StatusDto();
        statusDto.setGameState(GameState.IN_PROGRESS);

        when(gameService.updateGameState(any(), any())).thenReturn(gameDto);

        mockMvc.perform(put("/games/state/" + gameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(gameId.toString())));
    }

    @Test
    void updateGame_ShouldReturnOk() throws Exception {
        when(gamePersistenceService.updateGame(any(), any())).thenReturn(gameDto);

        mockMvc.perform(put("/games/" + gameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(gameId.toString())));
    }

    @Test
    void updateGameInGame_ShouldReturnOk() throws Exception {
        when(gameService.updateGameInGame(any(), any())).thenReturn(gameDataDto);

        mockMvc.perform(put("/games/in-game/" + gameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameDataDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(gameId.toString())));
    }

    @Test
    void updateGamePhase_ShouldReturnOk() throws Exception {
        when(gameService.nextPhase(any(), any())).thenReturn(gameDataDto);

        mockMvc.perform(put("/games/in-game-phase/" + gameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameDataDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(gameId.toString())));
    }

    @Test
    void joinGame_ShouldReturnOk() throws Exception {
        JoinGameRequestDto request = new JoinGameRequestDto();
        request.setUserId(userId);
        request.setPassword("password");

        when(gameService.joinGame(any(), any(), any())).thenReturn(gameDto);

        mockMvc.perform(put("/games/join/" + gameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(gameId.toString())));
    }
}
