package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.TerritoryDto;
import ar.edu.utn.frc.tup.piii.dtos.TerritoryStateDto;
import ar.edu.utn.frc.tup.piii.services.TerritoryStateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TerritoryStateController.class)
class TerritoryStateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TerritoryStateService service;

    @Autowired
    private ObjectMapper objectMapper;

    private TerritoryStateDto territoryStateDto;
    private TerritoryDto territoryDto;

    @BeforeEach
    void setUp() {
        territoryDto = new TerritoryDto();
        territoryDto.setTerritoryId(1L);
        territoryDto.setName("Argentina");

        territoryStateDto = new TerritoryStateDto();
        territoryStateDto.setTerritoryStateId(1L);
        territoryStateDto.setTerritory(territoryDto);
        territoryStateDto.setPlayer(99L);
        territoryStateDto.setArmyCount(5);
    }

    @Test
    void getByPlayer_shouldReturnList() throws Exception {
        when(service.findByPlayer(99L)).thenReturn(List.of(territoryStateDto));

        mockMvc.perform(get("/territoryState/player/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].territoryStateId").value(1))
                .andExpect(jsonPath("$[0].player").value(99))
                .andExpect(jsonPath("$[0].armyCount").value(5))
                .andExpect(jsonPath("$[0].territory.territoryId").value(1))
                .andExpect(jsonPath("$[0].territory.name").value("Argentina"));
    }

    @Test
    void create_shouldReturnCreatedTerritoryState() throws Exception {
        when(service.create(any(TerritoryStateDto.class))).thenReturn(territoryStateDto);

        mockMvc.perform(post("/territoryState")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(territoryStateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.territoryStateId").value(1))
                .andExpect(jsonPath("$.player").value(99))
                .andExpect(jsonPath("$.armyCount").value(5))
                .andExpect(jsonPath("$.territory.territoryId").value(1))
                .andExpect(jsonPath("$.territory.name").value("Argentina"));
    }

    @Test
    void update_shouldReturnUpdatedTerritoryState() throws Exception {
        TerritoryStateDto updatedDto = new TerritoryStateDto();
        updatedDto.setTerritoryStateId(1L);
        updatedDto.setTerritory(territoryDto);
        updatedDto.setPlayer(99L);
        updatedDto.setArmyCount(10);

        when(service.update(eq(1L), any(TerritoryStateDto.class))).thenReturn(updatedDto);

        mockMvc.perform(put("/territoryState/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(territoryStateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.territoryStateId").value(1))
                .andExpect(jsonPath("$.player").value(99))
                .andExpect(jsonPath("$.armyCount").value(10))
                .andExpect(jsonPath("$.territory.territoryId").value(1))
                .andExpect(jsonPath("$.territory.name").value("Argentina"));
    }
}