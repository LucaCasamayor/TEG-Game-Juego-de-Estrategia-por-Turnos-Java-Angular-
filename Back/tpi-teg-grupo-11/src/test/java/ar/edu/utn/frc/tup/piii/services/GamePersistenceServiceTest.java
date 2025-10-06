package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.GameDto;
import ar.edu.utn.frc.tup.piii.models.Game;
import ar.edu.utn.frc.tup.piii.persistence.GamePersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GamePersistenceServiceTest {

    @Mock
    private GamePersistence persistence;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GamePersistenceService gamePersistenceService;

    private UUID gameId;
    private GameDto gameDto;
    private Game game;
    private Game updatedGame;
    private GameDto updatedGameDto;

    @BeforeEach
    void setUp() {
        gameId = UUID.randomUUID();

        // Setup test data
        gameDto = new GameDto();
        // Add properties to gameDto as needed based on your actual GameDto class

        game = new Game();
        // Add properties to game as needed based on your actual Game class

        updatedGame = new Game();
        // Add properties to updatedGame as needed based on your actual Game class

        updatedGameDto = new GameDto();
        // Add properties to updatedGameDto as needed based on your actual GameDto class
    }

    @Test
    void updateGame_ShouldReturnUpdatedGameDto_WhenValidInput() {
        // Given
        when(modelMapper.map(gameDto, Game.class)).thenReturn(game);
        when(persistence.updateGame(gameId, game)).thenReturn(updatedGame);
        when(modelMapper.map(updatedGame, GameDto.class)).thenReturn(updatedGameDto);

        // When
        GameDto result = gamePersistenceService.updateGame(gameId, gameDto);

        // Then
        assertNotNull(result);
        assertEquals(updatedGameDto, result);

        // Verify interactions
        verify(modelMapper).map(gameDto, Game.class);
        verify(persistence).updateGame(gameId, game);
        verify(modelMapper).map(updatedGame, GameDto.class);
        verifyNoMoreInteractions(modelMapper, persistence);
    }

    @Test
    void updateGame_ShouldHandleNullGameDto() {
        // Given
        GameDto nullGameDto = null;
        when(modelMapper.map(nullGameDto, Game.class)).thenReturn(null);
        when(persistence.updateGame(eq(gameId), any())).thenReturn(updatedGame);
        when(modelMapper.map(updatedGame, GameDto.class)).thenReturn(updatedGameDto);

        // When
        GameDto result = gamePersistenceService.updateGame(gameId, nullGameDto);

        // Then
        assertNotNull(result);
        assertEquals(updatedGameDto, result);

        verify(modelMapper).map(nullGameDto, Game.class);
        verify(persistence).updateGame(eq(gameId), any());
        verify(modelMapper).map(updatedGame, GameDto.class);
    }

    @Test
    void updateGame_ShouldHandleNullGameId() {
        // Given
        UUID nullGameId = null;
        when(modelMapper.map(gameDto, Game.class)).thenReturn(game);
        when(persistence.updateGame(nullGameId, game)).thenReturn(updatedGame);
        when(modelMapper.map(updatedGame, GameDto.class)).thenReturn(updatedGameDto);

        // When
        GameDto result = gamePersistenceService.updateGame(nullGameId, gameDto);

        // Then
        assertNotNull(result);
        assertEquals(updatedGameDto, result);

        verify(modelMapper).map(gameDto, Game.class);
        verify(persistence).updateGame(nullGameId, game);
        verify(modelMapper).map(updatedGame, GameDto.class);
    }

    @Test
    void updateGame_ShouldPropagateExceptionFromPersistence() {
        // Given
        RuntimeException expectedException = new RuntimeException("Database error");
        when(modelMapper.map(gameDto, Game.class)).thenReturn(game);
        when(persistence.updateGame(gameId, game)).thenThrow(expectedException);

        // When & Then
        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> gamePersistenceService.updateGame(gameId, gameDto));

        assertEquals("Database error", thrownException.getMessage());
        assertEquals(expectedException, thrownException);

        verify(modelMapper).map(gameDto, Game.class);
        verify(persistence).updateGame(gameId, game);
        verify(modelMapper, never()).map(any(Game.class), eq(GameDto.class));
    }

    @Test
    void updateGame_ShouldPropagateExceptionFromModelMapper() {
        // Given
        RuntimeException expectedException = new RuntimeException("Mapping error");
        when(modelMapper.map(gameDto, Game.class)).thenThrow(expectedException);

        // When & Then
        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> gamePersistenceService.updateGame(gameId, gameDto));

        assertEquals("Mapping error", thrownException.getMessage());
        assertEquals(expectedException, thrownException);

        verify(modelMapper).map(gameDto, Game.class);
        verify(persistence, never()).updateGame(any(), any());
    }

    @Test
    void getGameById_ShouldReturnGame_WhenValidGameId() {
        // Given
        when(persistence.getGameById(gameId)).thenReturn(game);

        // When
        Game result = gamePersistenceService.getGameById(gameId);

        // Then
        assertNotNull(result);
        assertEquals(game, result);

        verify(persistence).getGameById(gameId);
        verifyNoMoreInteractions(persistence);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void getGameById_ShouldReturnNull_WhenGameNotFound() {
        // Given
        when(persistence.getGameById(gameId)).thenReturn(null);

        // When
        Game result = gamePersistenceService.getGameById(gameId);

        // Then
        assertNull(result);

        verify(persistence).getGameById(gameId);
        verifyNoMoreInteractions(persistence);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void getGameById_ShouldHandleNullGameId() {
        // Given
        UUID nullGameId = null;
        when(persistence.getGameById(nullGameId)).thenReturn(null);

        // When
        Game result = gamePersistenceService.getGameById(nullGameId);

        // Then
        assertNull(result);

        verify(persistence).getGameById(nullGameId);
        verifyNoMoreInteractions(persistence);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void getGameById_ShouldPropagateExceptionFromPersistence() {
        // Given
        RuntimeException expectedException = new RuntimeException("Database connection failed");
        when(persistence.getGameById(gameId)).thenThrow(expectedException);

        // When & Then
        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> gamePersistenceService.getGameById(gameId));

        assertEquals("Database connection failed", thrownException.getMessage());
        assertEquals(expectedException, thrownException);

        verify(persistence).getGameById(gameId);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void constructor_ShouldCreateInstance_WithRequiredDependencies() {
        // Given
        GamePersistence mockPersistence = mock(GamePersistence.class);
        ModelMapper mockModelMapper = mock(ModelMapper.class);

        // When
        GamePersistenceService service = new GamePersistenceService(mockPersistence, mockModelMapper);

        // Then
        assertNotNull(service);
    }

    @Test
    void updateGame_ShouldHandleComplexMappingScenarios() {
        // Given - Testing with different mapping scenarios
        Game mappedGame = new Game();
        Game returnedGame = new Game();
        GameDto finalDto = new GameDto();

        when(modelMapper.map(gameDto, Game.class)).thenReturn(mappedGame);
        when(persistence.updateGame(gameId, mappedGame)).thenReturn(returnedGame);
        when(modelMapper.map(returnedGame, GameDto.class)).thenReturn(finalDto);

        // When
        GameDto result = gamePersistenceService.updateGame(gameId, gameDto);

        // Then
        assertNotNull(result);
        assertSame(finalDto, result);

        // Verify the exact sequence of operations
        var inOrder = inOrder(modelMapper, persistence);
        inOrder.verify(modelMapper).map(gameDto, Game.class);
        inOrder.verify(persistence).updateGame(gameId, mappedGame);
        inOrder.verify(modelMapper).map(returnedGame, GameDto.class);
        inOrder.verifyNoMoreInteractions();
    }
}