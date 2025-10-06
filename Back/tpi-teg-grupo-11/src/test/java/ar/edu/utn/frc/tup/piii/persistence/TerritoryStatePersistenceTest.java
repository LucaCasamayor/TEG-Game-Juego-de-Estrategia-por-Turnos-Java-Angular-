package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.PlayerEntity;
import ar.edu.utn.frc.tup.piii.entities.TerritoryStateEntity;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryStateMapper;
import ar.edu.utn.frc.tup.piii.models.TerritoryState;
import ar.edu.utn.frc.tup.piii.repository.jpa.TerritoryStateJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TerritoryStatePersistenceTest {

    @Mock
    private TerritoryStateJpaRepository repository;

    @Mock
    private TerritoryStateMapper mapper;

    @InjectMocks
    private TerritoryStatePersistence persistence;

    @Test
    void findTerritoryStateByPlayer() {
        Long playerId = 1L;
        TerritoryStateEntity entity = new TerritoryStateEntity();
        TerritoryState model = new TerritoryState();

        when(repository.findAllByPlayer_PlayerId(playerId)).thenReturn(List.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        List<TerritoryState> result = persistence.findTerritoryStateByPlayer(playerId);

        assertEquals(1, result.size());
        assertEquals(model, result.get(0));
    }

    @Test
    void findById() {
        Long id = 1L;
        TerritoryStateEntity entity = new TerritoryStateEntity();
        TerritoryState model = new TerritoryState();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        TerritoryState result = persistence.findById(id);

        assertEquals(model, result);
    }
    @Test
    void testFindById_NotFound() {
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            persistence.findById(id);
        });
    }

    @Test
    void createTerritoryState() {
        TerritoryState model = new TerritoryState();
        TerritoryStateEntity entity = new TerritoryStateEntity();
        TerritoryStateEntity savedEntity = new TerritoryStateEntity();
        TerritoryState mappedModel = new TerritoryState();

        when(mapper.toEntity(model)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toModel(savedEntity)).thenReturn(mappedModel);

        TerritoryState result = persistence.createTerritoryState(model);

        assertEquals(mappedModel, result);
    }

    @Test
    void updateTerritoryState() {
        Long id = 1L;
        TerritoryState inputModel = new TerritoryState();

        TerritoryStateEntity existingEntity = new TerritoryStateEntity();
        existingEntity.setArmyCount(5);

        TerritoryStateEntity updatedEntity = new TerritoryStateEntity();
        updatedEntity.setArmyCount(10);

        PlayerEntity playerEntity = new PlayerEntity();
        updatedEntity.setPlayer(playerEntity);

        TerritoryState expectedModel = new TerritoryState();

        when(repository.getReferenceById(id)).thenReturn(existingEntity);
        when(mapper.toEntity(inputModel)).thenReturn(updatedEntity);
        when(repository.save(existingEntity)).thenReturn(existingEntity);
        when(mapper.toModel(existingEntity)).thenReturn(expectedModel);

        TerritoryState result = persistence.updateTerritoryState(id, inputModel);

        assertEquals(expectedModel, result);
        assertEquals(10, existingEntity.getArmyCount());
        assertEquals(playerEntity, existingEntity.getPlayer());
    }

}