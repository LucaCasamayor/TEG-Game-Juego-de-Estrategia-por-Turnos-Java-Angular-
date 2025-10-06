package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.TurnDto;
import ar.edu.utn.frc.tup.piii.models.Turn;
import ar.edu.utn.frc.tup.piii.services.implementations.TurnServiceCommon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TurnController.class)
class TurnControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TurnServiceCommon turnService;

    @MockBean
    private ModelMapper modelMapper;

    private Turn turn;
    private TurnDto turnDto;

    @BeforeEach
    void setUp() {
        turn = new Turn();
        turn.setTurnId(1L);

        turnDto = new TurnDto();
        turnDto.setTurnId(1L);
    }

    @Test
    void getTurnsByGame_shouldReturnList() throws Exception {
        when(turnService.getTurnsByGame("game-123")).thenReturn(List.of(turn));
        when(modelMapper.map(any(Turn.class), eq(TurnDto.class))).thenReturn(turnDto);

        mockMvc.perform(get("/turnos/game/game-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].turnId").value(1));
    }

    @Test
    void didPlayerConquer_shouldReturnTrue() throws Exception {
        when(turnService.didPlayerConquerThisTurn(99L, 1L)).thenReturn(true);

        mockMvc.perform(get("/turnos/1/player/99/did-conquer"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void didPlayerConquer_shouldReturnFalse() throws Exception {
        when(turnService.didPlayerConquerThisTurn(99L, 1L)).thenReturn(false);

        mockMvc.perform(get("/turnos/1/player/99/did-conquer"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
