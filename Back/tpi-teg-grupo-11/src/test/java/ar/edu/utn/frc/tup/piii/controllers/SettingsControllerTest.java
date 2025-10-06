package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.GameMapDto;
import ar.edu.utn.frc.tup.piii.dtos.ObjectiveTypeDto;
import ar.edu.utn.frc.tup.piii.dtos.SettingsDto;
import ar.edu.utn.frc.tup.piii.enums.AIProfile;
import ar.edu.utn.frc.tup.piii.services.SettingsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SettingsController.class)
class SettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SettingsService settingsService;

    @Autowired
    private ObjectMapper objectMapper;

    private SettingsDto settingsDto;
    private GameMapDto gameMapDto;
    private ObjectiveTypeDto objectiveTypeDto;

    @BeforeEach
    void setUp() {
        gameMapDto = new GameMapDto();

        objectiveTypeDto = new ObjectiveTypeDto();

        settingsDto = new SettingsDto();
        settingsDto.setSettingsId(1L);
        settingsDto.setTurnTime(60);
        settingsDto.setMap(gameMapDto);
        settingsDto.setAiProfile(AIProfile.STUDENTS);
        settingsDto.setObjectiveTypes(List.of(objectiveTypeDto));
        settingsDto.setPassword("secret123");
        settingsDto.setPrivate(true);
    }

    @Test
    void getSettingsById_shouldReturnSettings() throws Exception {
        when(settingsService.findById(1L)).thenReturn(settingsDto);

        mockMvc.perform(get("/settings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.settingsId").value(1))
                .andExpect(jsonPath("$.turnTime").value(60))
                .andExpect(jsonPath("$.password").value("secret123"))
                .andExpect(jsonPath("$.isPrivate").value(true))
                .andExpect(jsonPath("$.objectiveTypes").isArray());
    }

    @Test
    void getSettingsById_shouldReturn404WhenNotFound() throws Exception {
        when(settingsService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/settings/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveSettings_shouldReturnCreatedSettings() throws Exception {
        when(settingsService.createSettings(any(SettingsDto.class))).thenReturn(settingsDto);

        mockMvc.perform(post("/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(settingsDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.settingsId").value(1))
                .andExpect(jsonPath("$.turnTime").value(60))
                .andExpect(jsonPath("$.password").value("secret123"))
                .andExpect(jsonPath("$.isPrivate").value(true))
                .andExpect(jsonPath("$.objectiveTypes").isArray());
    }

    @Test
    void saveSettings_shouldReturn400WhenServiceReturnsNull() throws Exception {
        when(settingsService.createSettings(any(SettingsDto.class))).thenReturn(null);

        mockMvc.perform(post("/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(settingsDto)))
                .andExpect(status().isBadRequest());
    }
}