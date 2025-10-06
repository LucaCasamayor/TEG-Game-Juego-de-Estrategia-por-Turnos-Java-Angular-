package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.AICharacterDto;
import ar.edu.utn.frc.tup.piii.enums.AIProfile;
import ar.edu.utn.frc.tup.piii.services.AICharacterService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AICharacterController.class)
public class AICharacterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AICharacterService aiCharacterService;

    @Autowired
    private ObjectMapper objectMapper;

    private AICharacterDto createMockCharacter() {
        AICharacterDto dto = new AICharacterDto();
        dto.setCharacterId(1L);
        dto.setName("Botazo");
        dto.setProfile(AIProfile.TEACHERS);
        return dto;
    }

    @Test
    @DisplayName("GET /ai-characters/getAll - should return all AI characters")
    void testGetAll() throws Exception {
        List<AICharacterDto> characters = List.of(createMockCharacter());

        when(aiCharacterService.getAll()).thenReturn(characters);

        mockMvc.perform(get("/ai-characters/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(characters)));
    }

    @Test
    @DisplayName("GET /ai-characters/getById/{id} - should return character by ID")
    void testGetById() throws Exception {
        AICharacterDto character = createMockCharacter();

        when(aiCharacterService.getById(1L)).thenReturn(character);

        mockMvc.perform(get("/ai-characters/getById/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(character)));
    }

    @Test
    @DisplayName("GET /ai-characters/getByProfile/{profile} - should return characters by profile")
    void testGetByProfile() throws Exception {
        List<AICharacterDto> characters = List.of(createMockCharacter());

        when(aiCharacterService.getAllByProfile(AIProfile.TEACHERS)).thenReturn(characters);

        mockMvc.perform(get("/ai-characters/getByProfile/TEACHERS"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(characters)));
    }
}
