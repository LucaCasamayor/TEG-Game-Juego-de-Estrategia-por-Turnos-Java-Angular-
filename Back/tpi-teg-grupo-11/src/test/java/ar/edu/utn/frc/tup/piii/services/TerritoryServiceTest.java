package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.TerritoryDto;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryMapper;
import ar.edu.utn.frc.tup.piii.models.Territory;
import ar.edu.utn.frc.tup.piii.persistence.TerritoryPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TerritoryServiceTest {

    private TerritoryPersistence persistence;
    private TerritoryMapper mapper;
    private TerritoryService service;

    @BeforeEach
    void setUp() {
        persistence = mock(TerritoryPersistence.class);
        mapper = mock(TerritoryMapper.class);
        service = new TerritoryService(persistence, mapper);
    }

    @Test
    void getAllByGameId_returnsMappedList() {
        Territory territory = new Territory();
        TerritoryDto dto = new TerritoryDto();

        when(persistence.findTerritoriesByMap(10L)).thenReturn(List.of(territory));
        when(mapper.toDto(territory)).thenReturn(dto);

        List<TerritoryDto> result = service.getAllByGameId(10L);

        assertEquals(1, result.size());
        verify(persistence).findTerritoriesByMap(10L);
        verify(mapper).toDto(territory);
    }

    @Test
    void getAllByRegionId_returnsMappedList() {
        Territory territory = new Territory();
        TerritoryDto dto = new TerritoryDto();

        when(persistence.findTerritoriesByRegion(5L)).thenReturn(List.of(territory));
        when(mapper.toDto(territory)).thenReturn(dto);

        List<TerritoryDto> result = service.getAllByRegionId(5L);

        assertEquals(1, result.size());
        verify(persistence).findTerritoriesByRegion(5L);
        verify(mapper).toDto(territory);
    }

    @Test
    void getById_returnsMappedTerritory() {
        Territory territory = new Territory();
        TerritoryDto dto = new TerritoryDto();

        when(persistence.findById(3L)).thenReturn(territory);
        when(mapper.toDto(territory)).thenReturn(dto);

        TerritoryDto result = service.getById(3L);

        assertEquals(dto, result);
        verify(persistence).findById(3L);
        verify(mapper).toDto(territory);
    }

    @Test
    void getBorderingCountries_returnsList() {
        Territory border1 = new Territory();
        Territory border2 = new Territory();

        when(persistence.getBorderingTerritories(7L)).thenReturn(List.of(border1, border2));

        List<Territory> result = service.getBorderingCountries(7L);

        assertEquals(2, result.size());
        verify(persistence).getBorderingTerritories(7L);
    }
}
