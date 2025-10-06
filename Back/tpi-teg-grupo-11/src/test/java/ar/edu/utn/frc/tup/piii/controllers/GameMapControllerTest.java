package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.GameMapDto;
import ar.edu.utn.frc.tup.piii.services.GameMapService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameMapController.class)
class GameMapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameMapService gameMapService;

    @Autowired
    private ObjectMapper objectMapper;

    private GameMapDto createGameMapDto() {
        GameMapDto map = new GameMapDto();
        map.setId(1L);
        map.setName("Mapa Test");
        return map;
    }

    @Test
    @DisplayName("GET /maps - success")
    void testGetAllMaps_Success() throws Exception {
        List<GameMapDto> maps = List.of(createGameMapDto());

        when(gameMapService.getAll()).thenReturn(maps);

        mockMvc.perform(get("/maps")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(maps)));
    }

    @Test
    @DisplayName("GET /maps - not found")
    void testGetAllMaps_NotFound() throws Exception {
        when(gameMapService.getAll()).thenReturn(null);

        mockMvc.perform(get("/maps")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("not found"));

    }

    @Test
    @DisplayName("GET /maps/{id} - success")
    void testGetMapById_Success() throws Exception {
        GameMapDto map = createGameMapDto();

        when(gameMapService.getById(1L)).thenReturn(map);

        mockMvc.perform(get("/maps/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(map)));
    }

    @Test
    @DisplayName("GET /maps/{id} - not found")
    void testGetMapById_NotFound() throws Exception {
        when(gameMapService.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/maps/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("not found"));

    }
}
