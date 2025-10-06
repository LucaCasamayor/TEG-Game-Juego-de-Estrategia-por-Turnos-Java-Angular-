package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.dtos.GameDataDto;
import ar.edu.utn.frc.tup.piii.dtos.GameDto;
import ar.edu.utn.frc.tup.piii.entities.GameEntity;
import ar.edu.utn.frc.tup.piii.entities.PlayerEntity;
import ar.edu.utn.frc.tup.piii.entities.SettingsEntity;
import ar.edu.utn.frc.tup.piii.enums.GameState;
import ar.edu.utn.frc.tup.piii.mappers.GameMapper;
import ar.edu.utn.frc.tup.piii.mappers.PlayerMapper;
import ar.edu.utn.frc.tup.piii.mappers.SettingsMapper;
import ar.edu.utn.frc.tup.piii.mappers.TurnMapper;
import ar.edu.utn.frc.tup.piii.models.Game;
import ar.edu.utn.frc.tup.piii.models.Player;
import ar.edu.utn.frc.tup.piii.models.Settings;
import ar.edu.utn.frc.tup.piii.repository.jpa.GameJpaRepository;
import ar.edu.utn.frc.tup.piii.repository.jpa.SettingsJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GamePersistenceTest {

    @Mock
    private GameJpaRepository gameJpaRepository;

    @Mock
    private SettingsJpaRepository settingsJpaRepository;

    @Mock
    private GameMapper gameMapper;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private SettingsMapper settingsMapper;

    @Mock
    private TurnMapper turnMapper;

    @InjectMocks
    private GamePersistence gamePersistence;

    private UUID gameId;
    private Game game;
    private GameEntity gameEntity;
    private Settings settings;
    private SettingsEntity settingsEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameId = UUID.randomUUID();
        game = new Game();
        game.setGameId(gameId);
        settings = new Settings();
        settings.setSettingsId(1L);
        game.setSettings(settings);
        game.setPlayers(new ArrayList<>());
        game.setGameState(GameState.WAITING_PLAYERS);

        gameEntity = new GameEntity();
        gameEntity.setGameId(gameId);
        settingsEntity = new SettingsEntity();
        settingsEntity.setSettingsId(1L);
    }

    @Test
    void findGameDataById_ShouldReturnGameDataDto() {
        GameDataDto expectedDto = new GameDataDto();

        when(gameJpaRepository.findById(gameId)).thenReturn(Optional.of(gameEntity));
        when(gameMapper.toModel(gameEntity)).thenReturn(game);
        when(gameMapper.toGameData(game)).thenReturn(expectedDto);

        GameDataDto result = gamePersistence.findGameDataById(gameId);

        assertEquals(expectedDto, result);
        verify(gameJpaRepository).findById(gameId);
    }

    @Test
    void findGameDataById_WhenNotFound_ShouldThrowException() {
        when(gameJpaRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gamePersistence.findGameDataById(gameId));
    }

    @Test
    void findGameById_ShouldReturnGameDto() {
        GameDto expectedDto = new GameDto();

        when(gameJpaRepository.findById(gameId)).thenReturn(Optional.of(gameEntity));
        when(gameMapper.toModel(gameEntity)).thenReturn(game);
        when(settingsMapper.toDto(any())).thenReturn(expectedDto.getSettings());

        GameDto result = gamePersistence.findGameById(gameId);

        assertNotNull(result);
        verify(gameJpaRepository).findById(gameId);
    }

    @Test
    void createGame_ShouldReturnCreatedGame() {
        when(settingsJpaRepository.findById(settings.getSettingsId())).thenReturn(Optional.of(settingsEntity));
        when(gameMapper.toEntity(game)).thenReturn(gameEntity);
        when(gameJpaRepository.save(gameEntity)).thenReturn(gameEntity);
        when(gameMapper.toModel(gameEntity)).thenReturn(game);

        Game result = gamePersistence.createGame(game);

        assertEquals(game, result);
        verify(gameJpaRepository).save(gameEntity);
    }

    @Test
    void updateGame_ShouldReturnUpdatedGame() {
        when(gameJpaRepository.findByGameId(gameId)).thenReturn(Optional.of(gameEntity));
        when(gameJpaRepository.save(any())).thenReturn(gameEntity);
        when(gameMapper.toModel(any())).thenReturn(game);

        Game result = gamePersistence.updateGame(gameId, game);

        assertEquals(game, result);
        verify(gameJpaRepository).save(gameEntity);
    }

    @Test
    void findGameLobbies_ShouldReturnGames() {
        List<GameEntity> entities = List.of(gameEntity);
        when(gameJpaRepository.findByGameState(GameState.WAITING_PLAYERS)).thenReturn(entities);
        when(gameMapper.toModel(gameEntity)).thenReturn(game);

        List<Game> result = gamePersistence.findGameLobbies();

        assertEquals(1, result.size());
    }

    @Test
    void getGameById_ShouldReturnGame() {
        when(gameJpaRepository.findByGameId(gameId)).thenReturn(Optional.of(gameEntity));
        when(gameMapper.toModel(gameEntity)).thenReturn(game);

        Game result = gamePersistence.getGameById(gameId);

        assertEquals(game, result);
    }

    @Test
    void getGameById_WhenNotFound_ShouldThrowException() {
        when(gameJpaRepository.findByGameId(gameId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gamePersistence.getGameById(gameId));
    }

    @Test
    void joinGame_ShouldAddPlayerAndReturnGame() {
        Player player = new Player();
        player.setPlayerId(10L);

        game.setSettings(settings);
        game.setPlayers(new ArrayList<>());

        when(gameJpaRepository.findByGameId(gameId)).thenReturn(Optional.of(gameEntity));
        when(gameMapper.toModel(gameEntity)).thenReturn(game);
        when(gameMapper.toEntity(any(Game.class))).thenReturn(gameEntity);
        when(playerMapper.toEntity(any(Player.class))).thenReturn(new PlayerEntity()); // ajustar según implementación
        when(gameJpaRepository.save(any(GameEntity.class))).thenReturn(gameEntity);
        when(gameMapper.toModel(gameEntity)).thenReturn(game);

        Game result = gamePersistence.joinGame(gameId, player, null);

        assertEquals(game, result);
        verify(gameJpaRepository).save(any(GameEntity.class));
    }

    @Test
    void joinGame_WhenPlayerAlreadyInGame_ShouldThrowException() {
        Player player = new Player();
        player.setPlayerId(1L);
        game.setPlayers(List.of(player));

        when(gameJpaRepository.findByGameId(gameId)).thenReturn(Optional.of(gameEntity));
        when(gameMapper.toModel(gameEntity)).thenReturn(game);

        assertThrows(IllegalArgumentException.class, () -> gamePersistence.joinGame(gameId, player, null));
    }
}
