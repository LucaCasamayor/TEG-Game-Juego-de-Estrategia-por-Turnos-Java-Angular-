package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.TerritoryEntity;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryMapper;
import ar.edu.utn.frc.tup.piii.models.Territory;
import ar.edu.utn.frc.tup.piii.repository.jpa.TerritoryJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TerritoryPersistenceTest {

    @Mock
    private TerritoryJpaRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private TerritoryMapper territoryMapper;

    @InjectMocks
    private TerritoryPersistence persistence;

    private TerritoryEntity entity;
    private Territory model;

    @BeforeEach
    void setUp() {
        entity = new TerritoryEntity();
        entity.setTerritoryId(1L);
        entity.setName("Territory A");

        model = new Territory();
        model.setTerritoryId(1L);
        model.setName("Territory A");
    }

    @Test
    void testFindById() {
        when(repository.getReferenceById(1L)).thenReturn(entity);
        when(modelMapper.map(entity, Territory.class)).thenReturn(model);

        Territory result = persistence.findById(1L);

        assertEquals(model, result);
        verify(repository).getReferenceById(1L);
        verify(modelMapper).map(entity, Territory.class);
    }

    @Test
    void testFindAllTerritories() {
        when(repository.findAll()).thenReturn(List.of(entity));
        when(territoryMapper.toModel(entity)).thenReturn(model);

        List<Territory> result = persistence.findAllTerritories();

        assertEquals(1, result.size());
        assertEquals(model, result.get(0));
        verify(repository).findAll();
        verify(territoryMapper).toModel(entity);
    }

    @Test
    void testFindTerritoriesByRegion() {
        when(repository.getAllByRegion_RegionId(10L)).thenReturn(List.of(entity));
        when(territoryMapper.toModel(entity)).thenReturn(model);

        List<Territory> result = persistence.findTerritoriesByRegion(10L);

        assertEquals(1, result.size());
        assertEquals(model, result.get(0));
        verify(repository).getAllByRegion_RegionId(10L);
        verify(territoryMapper).toModel(entity);
    }

    @Test
    void testFindTerritoriesByMap() {
        when(repository.getAllByRegion_Map_GameMapId(100L)).thenReturn(List.of(entity));
        when(territoryMapper.toModel(entity)).thenReturn(model);

        List<Territory> result = persistence.findTerritoriesByMap(100L);

        assertEquals(1, result.size());
        assertEquals(model, result.get(0));
        verify(repository).getAllByRegion_Map_GameMapId(100L);
        verify(territoryMapper).toModel(entity);
    }

    @Test
    void testGetBorderingTerritories() {
        when(repository.findAllNeighbors(1L)).thenReturn(List.of(entity));
        when(territoryMapper.toModel(entity)).thenReturn(model);

        List<Territory> result = persistence.getBorderingTerritories(1L);

        assertEquals(1, result.size());
        assertEquals(model, result.get(0));
        verify(repository).findAllNeighbors(1L);
        verify(territoryMapper).toModel(entity);
    }

    @Test
    void testToEntityWithBorders_WithBorders() {
        Territory inputModel = new Territory();
        inputModel.setTerritoryId(1L);
        inputModel.setBordersId(List.of(2L, 3L));

        TerritoryEntity entityResult = new TerritoryEntity();

        TerritoryEntity border1 = new TerritoryEntity();
        border1.setTerritoryId(2L);

        TerritoryEntity border2 = new TerritoryEntity();
        border2.setTerritoryId(3L);

        when(territoryMapper.toEntity(inputModel)).thenReturn(entityResult);
        when(repository.findById(2L)).thenReturn(Optional.of(border1));
        when(repository.findById(3L)).thenReturn(Optional.of(border2));

        TerritoryEntity result = persistence.toEntityWithBorders(inputModel);

        assertNotNull(result);
        assertEquals(2, result.getBorders().size());
        assertTrue(result.getBorders().contains(border1));
        assertTrue(result.getBorders().contains(border2));
    }

    @Test
    void testToEntityWithBorders_NoBorders() {
        Territory inputModel = new Territory();
        inputModel.setTerritoryId(1L);
        inputModel.setBordersId(null);

        TerritoryEntity entityResult = new TerritoryEntity();

        when(territoryMapper.toEntity(inputModel)).thenReturn(entityResult);

        TerritoryEntity result = persistence.toEntityWithBorders(inputModel);

        assertNotNull(result);
        assertTrue(result.getBorders().isEmpty());
    }

    @Test
    void testToEntityWithBorders_BorderNotFound() {
        Territory inputModel = new Territory();
        inputModel.setTerritoryId(1L);
        inputModel.setBordersId(List.of(999L));

        when(territoryMapper.toEntity(inputModel)).thenReturn(new TerritoryEntity());
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> persistence.toEntityWithBorders(inputModel));
    }
}