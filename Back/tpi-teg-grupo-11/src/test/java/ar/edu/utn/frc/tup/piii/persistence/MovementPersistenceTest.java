package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.MovementEntity;
import ar.edu.utn.frc.tup.piii.models.Movement;
import ar.edu.utn.frc.tup.piii.models.Turn;
import ar.edu.utn.frc.tup.piii.repository.jpa.MovementJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovementPersistenceTest {

    private MovementJpaRepository repository;
    private TurnPersistence turnPersistence;
    private ModelMapper modelMapper;
    private MovementPersistence movementPersistence;

    @BeforeEach
    void setUp() {
        repository = mock(MovementJpaRepository.class);
        turnPersistence = mock(TurnPersistence.class);
        modelMapper = new ModelMapper();
        movementPersistence = new MovementPersistence(repository, turnPersistence, modelMapper);
    }

    @Test
    void getMovementById_ShouldReturnMappedMovement() {
        // Arrange
        Long id = 1L;
        MovementEntity entity = new MovementEntity();
        entity.setId(id);
        entity.setArmyCount(5);
        when(repository.getReferenceById(id)).thenReturn(entity);

        // Act
        Movement result = movementPersistence.getMovementById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(5, result.getArmyCount());
        verify(repository).getReferenceById(id);
    }

    @Test
    void saveMovement_ShouldSaveAndReturnMappedMovement() {
        // Arrange
        Movement movement = new Movement();
        movement.setId(1L);
        movement.setArmyCount(3);

        MovementEntity entity = modelMapper.map(movement, MovementEntity.class);
        when(repository.save(any(MovementEntity.class))).thenReturn(entity);

        // Act
        Movement result = movementPersistence.saveMovement(movement);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getArmyCount());
        verify(repository).save(any(MovementEntity.class));
    }

    @Test
    void findAllByGameId_ShouldReturnAllMovementsFromTurns() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        String gameIdStr = gameId.toString();

        Movement m1 = new Movement();
        m1.setId(1L);
        m1.setArmyCount(2);

        Movement m2 = new Movement();
        m2.setId(2L);
        m2.setArmyCount(3);

        Turn turn1 = new Turn();
        turn1.setMovements(List.of(m1));

        Turn turn2 = new Turn();
        turn2.setMovements(List.of(m2));

        when(turnPersistence.findByGame(gameId)).thenReturn(Arrays.asList(turn1, turn2));

        // Act
        List<Movement> result = movementPersistence.findAllByGameId(gameIdStr);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(m -> m.getId().equals(1L)));
        assertTrue(result.stream().anyMatch(m -> m.getId().equals(2L)));
        verify(turnPersistence).findByGame(gameId);
    }
}
