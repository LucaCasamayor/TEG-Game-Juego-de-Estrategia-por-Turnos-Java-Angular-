package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.RegionDto;
import ar.edu.utn.frc.tup.piii.dtos.TerritoryDto;
import ar.edu.utn.frc.tup.piii.services.TerritoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TerritoryController.class)
class TerritoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TerritoryService service;

    private TerritoryDto territoryDto;
    private RegionDto regionDto;

    @BeforeEach
    void setUp() {
        regionDto = new RegionDto();
        regionDto.setId(1L);
        regionDto.setName("South America");

        territoryDto = new TerritoryDto();
        territoryDto.setTerritoryId(1L);
        territoryDto.setName("Argentina");
        territoryDto.setRegion(regionDto);
        territoryDto.setBordersId(List.of(2L, 3L, 4L));
    }

    @Test
    void getAllByMapId_shouldReturnList() throws Exception {
        when(service.getAllByGameId(1L)).thenReturn(List.of(territoryDto));

        mockMvc.perform(get("/territories/map/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].territoryId").value(1))
                .andExpect(jsonPath("$[0].name").value("Argentina"))
                .andExpect(jsonPath("$[0].region.id").value(1))
                .andExpect(jsonPath("$[0].region.name").value("South America"))
                .andExpect(jsonPath("$[0].bordersId").isArray())
                .andExpect(jsonPath("$[0].bordersId[0]").value(2))
                .andExpect(jsonPath("$[0].bordersId[1]").value(3))
                .andExpect(jsonPath("$[0].bordersId[2]").value(4));
    }

    @Test
    void getAllByRegionId_shouldReturnList() throws Exception {
        when(service.getAllByRegionId(1L)).thenReturn(List.of(territoryDto));

        mockMvc.perform(get("/territories/region/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].territoryId").value(1))
                .andExpect(jsonPath("$[0].name").value("Argentina"))
                .andExpect(jsonPath("$[0].region.id").value(1))
                .andExpect(jsonPath("$[0].region.name").value("South America"))
                .andExpect(jsonPath("$[0].bordersId").isArray())
                .andExpect(jsonPath("$[0].bordersId.length()").value(3));
    }

    @Test
    void getById_shouldReturnTerritory() throws Exception {
        when(service.getById(1L)).thenReturn(territoryDto);

        mockMvc.perform(get("/territories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.territoryId").value(1))
                .andExpect(jsonPath("$.name").value("Argentina"))
                .andExpect(jsonPath("$.region.id").value(1))
                .andExpect(jsonPath("$.region.name").value("South America"))
                .andExpect(jsonPath("$.bordersId").isArray())
                .andExpect(jsonPath("$.bordersId.length()").value(3));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        when(service.getById(999L)).thenReturn(null);

        mockMvc.perform(get("/territories/999"))
                .andExpect(status().isNotFound());
    }
}