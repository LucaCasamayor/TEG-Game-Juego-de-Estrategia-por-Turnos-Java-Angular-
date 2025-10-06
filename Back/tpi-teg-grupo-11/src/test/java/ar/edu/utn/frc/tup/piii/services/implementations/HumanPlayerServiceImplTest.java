package ar.edu.utn.frc.tup.piii.services.implementations;

import ar.edu.utn.frc.tup.piii.dtos.PlayerDto;
import ar.edu.utn.frc.tup.piii.entities.RegionEntity;
import ar.edu.utn.frc.tup.piii.enums.Color;
import ar.edu.utn.frc.tup.piii.models.Player;
import ar.edu.utn.frc.tup.piii.persistence.PlayerPersistence;
import ar.edu.utn.frc.tup.piii.services.implementations.HumanPlayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HumanPlayerServiceImplTest {

    @Mock
    private PlayerPersistence persistence;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private HumanPlayerServiceImpl humanPlayerService;

    private UUID gameId;
    private PlayerDto playerDto;
    private Player player;
    private Player savedPlayer;

    @BeforeEach
    void setUp() {
        gameId = UUID.randomUUID();

        playerDto = new PlayerDto();
        playerDto.setGameId(gameId);

        player = new Player();
        player.setGameId(gameId);

        savedPlayer = new Player();
        savedPlayer.setGameId(gameId);
        savedPlayer.setPlayerColor(Color.RED);
    }

    @Test
    void testCreatePlayer_Success_FirstPlayer() {
        // Arrange
        when(modelMapper.map(playerDto, Player.class)).thenReturn(player);
        when(persistence.findPlayersByGameId(gameId)).thenReturn(Collections.emptyList());
        when(persistence.createPlayer(any(Player.class))).thenReturn(savedPlayer);
        when(modelMapper.map(savedPlayer, PlayerDto.class)).thenReturn(playerDto);

        // Act
        PlayerDto result = humanPlayerService.createPlayer(playerDto);

        // Assert
        assertNotNull(result);
        verify(modelMapper).map(playerDto, Player.class);
        verify(persistence).findPlayersByGameId(gameId);
        verify(persistence).createPlayer(any(Player.class));
        verify(modelMapper).map(savedPlayer, PlayerDto.class);

        // Verify that the first available color (RED) was assigned
        verify(persistence).createPlayer(argThat(p -> p.getPlayerColor() == Color.RED));
    }

    @Test
    void testCreatePlayer_Success_SecondPlayer() {
        // Arrange
        Player existingPlayer = new Player();
        existingPlayer.setPlayerColor(Color.RED);
        List<Player> existingPlayers = Arrays.asList(existingPlayer);

        when(modelMapper.map(playerDto, Player.class)).thenReturn(player);
        when(persistence.findPlayersByGameId(gameId)).thenReturn(existingPlayers);
        when(persistence.createPlayer(any(Player.class))).thenReturn(savedPlayer);
        when(modelMapper.map(savedPlayer, PlayerDto.class)).thenReturn(playerDto);

        // Act
        PlayerDto result = humanPlayerService.createPlayer(playerDto);

        // Assert
        assertNotNull(result);
        verify(persistence).createPlayer(argThat(p -> p.getPlayerColor() != Color.RED));
    }

    @Test
    void testCreatePlayer_Success_AllColorsExceptOneUsed() {
        // Arrange - Use all colors except YELLOW
        List<Player> existingPlayers = new ArrayList<>();
        Color[] allColors = Color.values();

        for (int i = 0; i < allColors.length - 1; i++) {
            Player existingPlayer = new Player();
            existingPlayer.setPlayerColor(allColors[i]);
            existingPlayers.add(existingPlayer);
        }

        when(modelMapper.map(playerDto, Player.class)).thenReturn(player);
        when(persistence.findPlayersByGameId(gameId)).thenReturn(existingPlayers);
        when(persistence.createPlayer(any(Player.class))).thenReturn(savedPlayer);
        when(modelMapper.map(savedPlayer, PlayerDto.class)).thenReturn(playerDto);

        // Act
        PlayerDto result = humanPlayerService.createPlayer(playerDto);

        // Assert
        assertNotNull(result);
        // Should assign the last available color
        Color lastAvailableColor = allColors[allColors.length - 1];
        verify(persistence).createPlayer(argThat(p -> p.getPlayerColor() == lastAvailableColor));
    }

    @Test
    void testCreatePlayer_ThrowsException_GameIdIsNull() {
        // Arrange
        Player playerWithoutGameId = new Player();
        playerWithoutGameId.setGameId(null);

        when(modelMapper.map(playerDto, Player.class)).thenReturn(playerWithoutGameId);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> humanPlayerService.createPlayer(playerDto));

        assertEquals("El juego es requerido para crear un jugador", exception.getMessage());
        verify(modelMapper).map(playerDto, Player.class);
        verify(persistence, never()).findPlayersByGameId(any());
        verify(persistence, never()).createPlayer(any());
    }

    @Test
    void testCreatePlayer_ThrowsException_NoColorsAvailable() {
        // Arrange - All colors are used
        List<Player> existingPlayers = new ArrayList<>();
        for (Color color : Color.values()) {
            Player existingPlayer = new Player();
            existingPlayer.setPlayerColor(color);
            existingPlayers.add(existingPlayer);
        }

        when(modelMapper.map(playerDto, Player.class)).thenReturn(player);
        when(persistence.findPlayersByGameId(gameId)).thenReturn(existingPlayers);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> humanPlayerService.createPlayer(playerDto));

        assertEquals("No quedan colores disponibles para este juego", exception.getMessage());
        verify(modelMapper).map(playerDto, Player.class);
        verify(persistence).findPlayersByGameId(gameId);
        verify(persistence, never()).createPlayer(any());
    }

    @Test
    void testGetPlayersByGame_Success_WithPlayers() {
        // Arrange
        Player player1 = new Player();
        player1.setGameId(gameId);

        Player player2 = new Player();
        player2.setGameId(gameId);

        List<Player> players = Arrays.asList(player1, player2);

        PlayerDto playerDto1 = new PlayerDto();

        PlayerDto playerDto2 = new PlayerDto();

        when(persistence.findPlayersByGameId(gameId)).thenReturn(players);
        when(modelMapper.map(player1, PlayerDto.class)).thenReturn(playerDto1);
        when(modelMapper.map(player2, PlayerDto.class)).thenReturn(playerDto2);

        // Act
        List<PlayerDto> result = humanPlayerService.getPlayersByGame(gameId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

    }

    @Test
    void testGetPlayersByGame_Success_EmptyList() {
        // Arrange
        when(persistence.findPlayersByGameId(gameId)).thenReturn(Collections.emptyList());

        // Act
        List<PlayerDto> result = humanPlayerService.getPlayersByGame(gameId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(persistence).findPlayersByGameId(gameId);
        verify(modelMapper, never()).map(any(Player.class), eq(PlayerDto.class));
    }

    @Test
    void testGetAll_Success_WithPlayers() {
        // Arrange
        List<PlayerDto> expectedPlayers = Arrays.asList(
                new PlayerDto(),
                new PlayerDto()
        );

        when(persistence.findAll()).thenReturn(expectedPlayers);

        // Act
        List<PlayerDto> result = humanPlayerService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedPlayers, result);
        verify(persistence).findAll();
    }

    @Test
    void testGetAll_Success_EmptyList() {
        // Arrange
        when(persistence.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<PlayerDto> result = humanPlayerService.getAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(persistence).findAll();
    }
    @Test
    void testCreatePlayer_VerifyColorAssignmentOrder() {
        // Arrange - Test that colors are assigned in enum order
        when(modelMapper.map(playerDto, Player.class)).thenReturn(player);
        when(persistence.findPlayersByGameId(gameId)).thenReturn(Collections.emptyList());
        when(persistence.createPlayer(any(Player.class))).thenReturn(savedPlayer);
        when(modelMapper.map(savedPlayer, PlayerDto.class)).thenReturn(playerDto);

        // Act
        humanPlayerService.createPlayer(playerDto);

        // Assert
        // Should assign the first color in the enum (assuming Color.values()[0] is the first)
        Color firstColor = Color.values()[0];
        verify(persistence).createPlayer(argThat(p -> p.getPlayerColor() == firstColor));
    }

    /**
     * Helper method to invoke the private getRegionBonus method using reflection
     */
    private int invokeGetRegionBonus(RegionEntity region) {
        try {
            var method = HumanPlayerServiceImpl.class.getDeclaredMethod("getRegionBonus", RegionEntity.class);
            method.setAccessible(true);
            return (int) method.invoke(humanPlayerService, region);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke getRegionBonus method", e);
        }
    }
}