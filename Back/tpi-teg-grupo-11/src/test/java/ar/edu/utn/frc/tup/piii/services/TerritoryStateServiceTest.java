package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.TerritoryStateDto;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryStateMapper;
import ar.edu.utn.frc.tup.piii.models.TerritoryState;
import ar.edu.utn.frc.tup.piii.persistence.TerritoryStatePersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TerritoryStateServiceTest {

    @Mock
    private TerritoryStatePersistence persistence;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private TerritoryStateMapper territoryStateMapper;

    @InjectMocks
    private TerritoryStateService service;

    private TerritoryState model;
    private TerritoryStateDto dto;

    @BeforeEach
    void setUp() {
        model = new TerritoryState();
        model.setTerritoryStateId(1L);

        dto = new TerritoryStateDto();
        dto.setTerritoryStateId(1L);
    }

    @Test
    void findByPlayer() {
        when(persistence.findTerritoryStateByPlayer(1L)).thenReturn(List.of(model));
        when(modelMapper.map(model, TerritoryStateDto.class)).thenReturn(dto);

        List<TerritoryStateDto> result = service.findByPlayer(1L);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(persistence).findTerritoryStateByPlayer(1L);
        verify(modelMapper).map(model, TerritoryStateDto.class);
    }

    @Test
    void findAllByRegionId() {
        when(persistence.findByRegionId(2L)).thenReturn(List.of(model));

        List<TerritoryState> result = service.findAllByRegionId(2L);

        assertEquals(1, result.size());
        assertEquals(model, result.get(0));
        verify(persistence).findByRegionId(2L);
    }

    @Test
    void create() {
        when(territoryStateMapper.toModel(dto)).thenReturn(model);
        when(persistence.createTerritoryState(model)).thenReturn(model);
        when(modelMapper.map(model, TerritoryStateDto.class)).thenReturn(dto);

        TerritoryStateDto result = service.create(dto);

        assertEquals(dto, result);
        verify(territoryStateMapper).toModel(dto);
        verify(persistence).createTerritoryState(model);
        verify(modelMapper).map(model, TerritoryStateDto.class);
    }

    @Test
    void update() {
        when(territoryStateMapper.toModel(dto)).thenReturn(model);
        when(persistence.updateTerritoryState(1L, model)).thenReturn(model);
        when(modelMapper.map(model, TerritoryStateDto.class)).thenReturn(dto);

        TerritoryStateDto result = service.update(1L, dto);

        assertEquals(dto, result);
        verify(territoryStateMapper).toModel(dto);
        verify(persistence).updateTerritoryState(1L, model);
        verify(modelMapper).map(model, TerritoryStateDto.class);
    }

    @Test
    void findByIdModel() {
        when(persistence.findById(1L)).thenReturn(model);

        TerritoryState result = service.findByIdModel(1L);

        assertEquals(model, result);
        verify(persistence).findById(1L);
    }

    @Test
    void updateModel() {
        when(persistence.updateTerritoryState(1L, model)).thenReturn(model);

        TerritoryState result = service.updateModel(1L, model);

        assertEquals(model, result);
        verify(persistence).updateTerritoryState(1L, model);
    }

    @Test
    void findByPlayerModel() {
        when(persistence.findTerritoryStateByPlayer(1L)).thenReturn(List.of(model));

        List<TerritoryState> result = service.findByPlayerModel(1L);

        assertEquals(1, result.size());
        assertEquals(model, result.get(0));
        verify(persistence).findTerritoryStateByPlayer(1L);
    }
}
