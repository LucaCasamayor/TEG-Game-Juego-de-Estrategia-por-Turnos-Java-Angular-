package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.enums.AIProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AiProfileController.class)
class AiProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /ai-profile - returns all AI profiles")
    void testGetAIProfiles() throws Exception {
        AIProfile[] expectedProfiles = AIProfile.values();

        mockMvc.perform(get("/ai-profile"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProfiles)));
    }
}
