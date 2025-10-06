package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.ObjectiveTypeDto;
import ar.edu.utn.frc.tup.piii.services.ObjectiveTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ObjectiveTypeController.class)
class ObjectiveTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObjectiveTypeService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectiveTypeDto createObjectiveTypeDto() {
        ObjectiveTypeDto dto = new ObjectiveTypeDto();
        dto.setObjectiveTypeId(1L);
        dto.setName("Conquistar 24 territorios");
        return dto;
    }

    @Test
    @DisplayName("GET /objective-types - OK")
    void testGetAllObjectiveTypes() throws Exception {
        List<ObjectiveTypeDto> objectiveTypes = List.of(createObjectiveTypeDto());

        when(service.getAll()).thenReturn(objectiveTypes);

        mockMvc.perform(get("/objective-types"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(objectiveTypes)));
    }

    @Test
    @DisplayName("GET /objective-types/{id} - OK")
    void testGetObjectiveTypeById() throws Exception {
        ObjectiveTypeDto dto = createObjectiveTypeDto();

        when(service.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/objective-types/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }
}
