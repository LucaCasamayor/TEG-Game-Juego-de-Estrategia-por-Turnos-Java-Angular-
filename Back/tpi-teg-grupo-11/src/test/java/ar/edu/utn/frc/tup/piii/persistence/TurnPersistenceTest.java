package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.MovementEntity;
import ar.edu.utn.frc.tup.piii.entities.TurnEntity;
import ar.edu.utn.frc.tup.piii.mappers.MovementMapper;
import ar.edu.utn.frc.tup.piii.mappers.TurnMapper;
import ar.edu.utn.frc.tup.piii.models.Movement;
import ar.edu.utn.frc.tup.piii.models.Turn;
import ar.edu.utn.frc.tup.piii.repository.jpa.TurnJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TurnPersistenceTest {

    private TurnJpaRepository repository;
    private ModelMapper modelMapper;
    private TurnMapper turnMapper;
    private TurnPersistence persistence;
    private MovementMapper movementMapper;

    @BeforeEach
    void setup() {
        repository = mock(TurnJpaRepository.class);
        modelMapper = mock(ModelMapper.class);
        turnMapper = mock(TurnMapper.class);
        movementMapper = mock(MovementMapper.class);
        persistence = new TurnPersistence(repository, modelMapper, turnMapper, movementMapper);
    }

    @Test
    void saveTurn_validTurn_returnsSavedTurn() {
        Turn turn = new Turn();
        TurnEntity entity = new TurnEntity();

        when(modelMapper.map(turn, TurnEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(modelMapper.map(entity, Turn.class)).thenReturn(turn);

        Turn result = persistence.saveTurn(turn);

        assertNotNull(result);
        verify(repository).save(entity);
    }

    @Test
    void findById_existingId_returnsTurn() {
        Long id = 1L;
        TurnEntity entity = new TurnEntity();
        Turn turn = new Turn();

        when(repository.getReferenceById(id)).thenReturn(entity);
        when(modelMapper.map(entity, Turn.class)).thenReturn(turn);

        Turn result = persistence.findById(id);

        assertNotNull(result);
        verify(repository).getReferenceById(id);
    }

    @Test
    void findByGame_existingGameId_returnsListOfTurns() {
        UUID gameId = UUID.randomUUID();
        TurnEntity entity = new TurnEntity();
        Turn turn = new Turn();

        when(repository.getAllByGame_GameId(gameId)).thenReturn(List.of(entity));
        when(modelMapper.map(entity, Turn.class)).thenReturn(turn);

        List<Turn> result = persistence.findByGame(gameId);

        assertEquals(1, result.size());
        verify(repository).getAllByGame_GameId(gameId);
    }

    @Test
    void updateTurn_existingTurn_updatesAndReturnsTurn() {
        Long id = 1L;
        TurnEntity entity = new TurnEntity();
        Turn turn = new Turn();
        Movement movement = new Movement();
        MovementEntity movementEntity = new MovementEntity();

        turn.setMovements(List.of(movement));

        when(repository.getReferenceById(id)).thenReturn(entity);
        when(modelMapper.map(movement, MovementEntity.class)).thenReturn(movementEntity);
        when(repository.save(entity)).thenReturn(entity);
        when(turnMapper.toModel(entity)).thenReturn(turn);

        Turn result = persistence.updateTurn(id, turn);

        assertNotNull(result);
        verify(repository).save(entity);
    }

    @Test
    void findByPlayer_existingPlayerId_returnsTurnsOfPlayer() {
        Long playerId = 1L;
        TurnEntity entity = new TurnEntity();
        Turn turn = new Turn();

        when(repository.findByPlayer_PlayerId(playerId)).thenReturn(List.of(entity));
        when(modelMapper.map(entity, Turn.class)).thenReturn(turn);

        List<Turn> result = persistence.findByPlayer(playerId);

        assertEquals(1, result.size());
        verify(repository).findByPlayer_PlayerId(playerId);
    }

}
