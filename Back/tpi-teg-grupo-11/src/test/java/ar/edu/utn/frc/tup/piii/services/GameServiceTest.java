package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.*;
import ar.edu.utn.frc.tup.piii.entities.GameEntity;
import ar.edu.utn.frc.tup.piii.entities.PlayerEntity;
import ar.edu.utn.frc.tup.piii.entities.TerritoryStateEntity;
import ar.edu.utn.frc.tup.piii.entities.UserEntity;
import ar.edu.utn.frc.tup.piii.enums.Color;
import ar.edu.utn.frc.tup.piii.enums.GameState;
import ar.edu.utn.frc.tup.piii.enums.PlayerType;
import ar.edu.utn.frc.tup.piii.enums.TurnPhase;
import ar.edu.utn.frc.tup.piii.mappers.GameMapper;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryStateMapper;
import ar.edu.utn.frc.tup.piii.models.*;
import ar.edu.utn.frc.tup.piii.persistence.*;
import ar.edu.utn.frc.tup.piii.repository.jpa.GameJpaRepository;
import ar.edu.utn.frc.tup.piii.repository.jpa.UserJpaRepository;
import ar.edu.utn.frc.tup.piii.services.implementations.TurnServiceCommon;
import ar.edu.utn.frc.tup.piii.services.interfaces.TurnService;
import ar.edu.utn.frc.tup.piii.services.interfaces.TurnStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static ar.edu.utn.frc.tup.piii.enums.GameState.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GamePersistence persistence;

    @Mock
    private PlayerPersistence playerPersistence;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ObjectiveService objectiveService;

    @Mock
    private TerritoryPersistence territoryPersistence;

    @Mock
    private TerritoryStateMapper territoryStateMapper;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private TerritoryStatePersistence territoryStatePersistence;

    @Mock
    private GameMapper gameMapper;

    @Mock
    private TurnStrategyFactory turnStrategyFactory;

    @Mock
    private UserPersistence userPersistence;

    @Mock
    private TurnServiceCommon turnServiceCommon;

    @Mock
    private TurnService mockTurnService;

    @Mock
    private GameJpaRepository gameJpaRepository;

    @InjectMocks
    private GameService gameService;

    private UUID gameId;
    private UUID userId;
    private Long playerId;
    private Game game;
    private GameEntity gameEntity;
    private User user;
    private Player player;
    private GameDto gameDto;
    private GameDataDto gameDataDto;
    private Turn turn;

    @BeforeEach
    void setUp() {
        gameId = UUID.randomUUID();
        userId = UUID.randomUUID();
        playerId = 1L;

        // Setup User
        user = new User();
        user.setUserId(userId);
        user.setUsername("testUser");

        // Setup Player
        player = new Player();
        player.setPlayerId(playerId);
        player.setUser(user);
        player.setPlayerType(PlayerType.PLAYER);
        player.setPlayerColor(Color.RED);
        player.setTurnOrder(1);
        player.setTerritories(new ArrayList<>());

        // Setup Game Settings
        Settings settings = new Settings();
        settings.setPrivate(false);
        settings.setPassword("password");
        settings.setObjectiveTypes(new ArrayList<>());

        // Setup Game
        game = new Game();
        game.setGameId(gameId);
        game.setSettings(settings);
        game.setGameState(WAITING_PLAYERS);
        game.setPlayers(new ArrayList<>(Arrays.asList(player)));

        gameEntity = new GameEntity();
        gameEntity.setGameId(game.getGameId());

        // Setup DTOs
        gameDto = new GameDto();
        gameDto.setGameId(gameId);

        turn = new Turn();
        turn.setTurnId(1L);
        turn.setPlayer(player);
        turn.setGameId(gameId);
        turn.setTurnPhase(TurnPhase.DEPLOYMENT);

        // Setup basic mocks for DTOs
        ar.edu.utn.frc.tup.piii.dtos.TurnDto turnDto = new ar.edu.utn.frc.tup.piii.dtos.TurnDto();
        PlayerDto playerDto = new PlayerDto();
        playerDto.setPlayerId(playerId);
        playerDto.setTerritories(new ArrayList<>());

        gameDataDto = new GameDataDto();
        gameDataDto.setGameId(gameId);
        gameDataDto.setCurrentTurn(turnDto);
        gameDataDto.setPlayers(Arrays.asList(playerDto));

        lenient().when(modelMapper.map(any(Turn.class), eq(TurnDto.class))).thenReturn(turnDto);
        lenient().when(modelMapper.map(any(Player.class), eq(PlayerDto.class))).thenReturn(playerDto);
    }

    @Test
    void findGameData_ShouldReturnGameDataDto() {
        // Arrange
        GameDataDto expectedGameData = new GameDataDto();
        when(persistence.findGameDataById(gameId)).thenReturn(expectedGameData);

        // Act
        GameDataDto result = gameService.findGameData(gameId);

        // Assert
        assertEquals(expectedGameData, result);
        verify(persistence).findGameDataById(gameId);
    }

    @Test
    void findGame_ShouldReturnGameDto() {
        // Arrange
        GameDto expectedGame = new GameDto();
        when(persistence.findGameById(gameId)).thenReturn(expectedGame);

        // Act
        GameDto result = gameService.findGame(gameId);

        // Assert
        assertEquals(expectedGame, result);
        verify(persistence).findGameById(gameId);
    }

    @Test
    void joinGame_WhenGameInProgress_ShouldThrowException() {
        // Arrange
        game.setGameState(IN_PROGRESS);
        when(persistence.getGameById(gameId)).thenReturn(game);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> gameService.joinGame(gameId, userId, null));
        assertEquals("La partida ya está en curso", exception.getMessage());
    }

    @Test
    void joinGame_WhenPrivateGameWithoutPassword_ShouldThrowException() {
        // Arrange
        game.getSettings().setPrivate(true);
        when(persistence.getGameById(gameId)).thenReturn(game);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> gameService.joinGame(gameId, userId, null));
        assertEquals("La partida es privada", exception.getMessage());
    }

    @Test
    void joinGame_WhenPrivateGameWithWrongPassword_ShouldThrowException() {
        // Arrange
        game.getSettings().setPrivate(true);
        game.getSettings().setPassword("correct");
        when(persistence.getGameById(gameId)).thenReturn(game);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> gameService.joinGame(gameId, userId, "wrong"));
        assertEquals("Contraseña incorrecta", exception.getMessage());
    }

    @Test
    void joinGame_WhenUserAlreadyJoined_ShouldThrowException() {
        // Arrange
        when(persistence.getGameById(gameId)).thenReturn(game);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> gameService.joinGame(gameId, userId, null));
        assertEquals("Usuario ya está en la partida", exception.getMessage());
    }

    @Test
    void joinGame_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        UUID newUserId = UUID.randomUUID();
        when(persistence.getGameById(gameId)).thenReturn(game);
        when(userPersistence.findById(newUserId)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> gameService.joinGame(gameId, newUserId, null));
        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void joinGame_WhenSuccessful_ShouldReturnGameDto() {
        // Arrange
        UUID newUserId = UUID.randomUUID();
        User newUser = new User();
        newUser.setUserId(newUserId);

        Player newPlayer = new Player();
        newPlayer.setPlayerId(1L);
        newPlayer.setUser(newUser);
        newPlayer.setPlayerType(PlayerType.PLAYER);
        newPlayer.setTurnOrder(2);

        when(persistence.getGameById(gameId)).thenReturn(game);
        when(userPersistence.findById(newUserId)).thenReturn(newUser);
        when(playerPersistence.createPlayer(any(Player.class))).thenReturn(newPlayer);
        when(persistence.updateGame(eq(gameId), any(Game.class))).thenReturn(game);
        when(gameMapper.toDto(game)).thenReturn(gameDto);

        // Act
        GameDto result = gameService.joinGame(gameId, newUserId, null);

        // Assert
        assertEquals(gameDto, result);
        verify(playerPersistence).createPlayer(any(Player.class));
        verify(persistence).updateGame(eq(gameId), any(Game.class));
    }

    @Test
    void createGame_WhenPlayerExists_ShouldReturnGameDto() {
        // Arrange
        player.setPlayerId(playerId); // Player already exists
        gameDto.setPlayers(Arrays.asList(modelMapper.map(player, PlayerDto.class)));

        when(modelMapper.map(gameDto, Game.class)).thenReturn(game);
        when(persistence.createGame(any(Game.class))).thenReturn(game);
        when(modelMapper.map(game, GameDto.class)).thenReturn(gameDto);

        // Act
        GameDto result = gameService.createGame(gameDto);

        // Assert
        assertEquals(gameDto, result);
        verify(persistence).createGame(any(Game.class));
    }

    @Test
    void createGame_WhenPlayerDoesNotExist_ShouldCreateNewPlayer() {
        // Arrange
        player.setPlayerId(null); // Player doesn't exist
        gameDto.setPlayers(Arrays.asList(modelMapper.map(player, PlayerDto.class)));

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);

        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setUser(userEntity);

        when(modelMapper.map(gameDto, Game.class)).thenReturn(game);
        when(userJpaRepository.findByUserId(userId)).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(playerEntity, Player.class)).thenReturn(player);
        when(persistence.createGame(any(Game.class))).thenReturn(game);
        when(modelMapper.map(game, GameDto.class)).thenReturn(gameDto);

        // Act
        GameDto result = gameService.createGame(gameDto);

        // Assert
        assertEquals(gameDto, result);
        verify(userJpaRepository).findByUserId(userId);
        verify(persistence).createGame(any(Game.class));
    }

    @Test
    void createGame_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        player.setPlayerId(null);
        gameDto.setPlayers(Arrays.asList(modelMapper.map(player, PlayerDto.class)));

        when(modelMapper.map(gameDto, Game.class)).thenReturn(game);
        when(userJpaRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> gameService.createGame(gameDto));
    }

    @Test
    void startGame_ShouldInitializeGameAndReturnGame() {
        // Arrange
        List<Territory> territories = Arrays.asList(new Territory(), new Territory());
        List<ObjectiveTypeDto> objectiveTypes = new ArrayList<>();
        List<ar.edu.utn.frc.tup.piii.dtos.ObjectiveDto> objectives = new ArrayList<>();

        Objective objective = new Objective();
        objective.setColor(Color.BLUE); // Different from player color

        when(persistence.getGameById(gameId)).thenReturn(game);
        when(territoryPersistence.findAllTerritories()).thenReturn(territories);
        when(objectiveService.getAllByObjectiveTypes(any())).thenReturn(objectives);
        when(turnServiceCommon.createTurn(any(Turn.class))).thenReturn(turn);
        when(territoryStatePersistence.createTerritoryState(any(TerritoryState.class))).thenReturn(new TerritoryState());
        when(territoryStatePersistence.findTerritoryStateByPlayer(any())).thenReturn(Arrays.asList(new TerritoryState(), new TerritoryState()));
        when(territoryStatePersistence.updateTerritoryState(any(), any(TerritoryState.class))).thenReturn(new TerritoryState());
        when(persistence.updateGame(eq(gameId), any(Game.class))).thenReturn(game);

        // Act
        Game result = gameService.startGame(gameId);

        // Assert
        assertEquals(game, result);
        verify(turnServiceCommon).createTurn(any(Turn.class));
        verify(persistence).updateGame(eq(gameId), any(Game.class));
    }

    @Test
    void updateGameState_WhenWaitingPlayers_ShouldStartGame() {
        // Arrange
        when(persistence.getGameById(gameId)).thenReturn(game);
        setupStartGameMocks();
        when(gameMapper.toDto(any(Game.class))).thenReturn(gameDto);

        // Act
        GameDto result = gameService.updateGameState(gameId, WAITING_PLAYERS);

        // Assert
        assertEquals(gameDto, result);
    }

    @Test
    void updateGameState_WhenInProgress_ShouldReturnGame() {
        // Arrange
        when(persistence.getGameById(gameId)).thenReturn(game);
        when(gameMapper.toDto(game)).thenReturn(gameDto);

        // Act
        GameDto result = gameService.updateGameState(gameId, IN_PROGRESS);

        // Assert
        assertEquals(gameDto, result);
    }

    @Test
    void updateGameState_WhenPaused_ShouldReturnGame() {
        // Arrange
        when(persistence.getGameById(gameId)).thenReturn(game);
        when(gameMapper.toDto(game)).thenReturn(gameDto);

        // Act
        GameDto result = gameService.updateGameState(gameId, PAUSED);

        // Assert
        assertEquals(gameDto, result);
    }

    @Test
    void updateGameState_WhenUnhandledState_ShouldThrowException() {
        // Arrange
        when(persistence.getGameById(gameId)).thenReturn(game);

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> gameService.updateGameState(gameId, FINISHED));
    }

    @Test
    void updateGameInGame_ShouldCallPlayCurrentPhase() throws Exception {

        setupPlayCurrentPhaseMocks();

        when(gameMapper.toGameData(any(Game.class))).thenReturn(gameDataDto);

        GameDataDto result = gameService.updateGameInGame(gameId, gameDataDto);

        assertEquals(gameDataDto, result);
    }

    @Test
    void playCurrentPhase_WithDeploymentPhase_ShouldCallDeployStage() throws Exception {
        // Arrange
        turn.setTurnPhase(TurnPhase.DEPLOYMENT);
        setupPlayCurrentPhaseMocks();
        when(gameMapper.toGameData(any(Game.class))).thenReturn(gameDataDto);

        GameDataDto result = gameService.playCurrentPhase(gameId, gameDataDto);

        verify(mockTurnService).playDeployStage(any(Turn.class));
        assertEquals(gameDataDto, result);
    }

    @Test
    void playCurrentPhase_WithAttackPhase_ShouldCallAttackStage() throws Exception {
        // Arrange
        turn.setTurnPhase(TurnPhase.ATTACK);
        setupPlayCurrentPhaseMocks();

        when(gameMapper.toGameData(any(Game.class))).thenReturn(gameDataDto);

        GameDataDto result = gameService.playCurrentPhase(gameId, gameDataDto);

        verify(mockTurnService).playAttackStage(any(Turn.class));
        assertEquals(gameDataDto, result);
    }

    @Test
    void playCurrentPhase_WithFortifyPhase_ShouldCallFortifyStage() throws Exception {

        turn.setTurnPhase(TurnPhase.FORTIFY);
        setupPlayCurrentPhaseMocks();
        when(gameMapper.toGameData(any(Game.class))).thenReturn(gameDataDto);

        GameDataDto result = gameService.playCurrentPhase(gameId, gameDataDto);

        verify(mockTurnService).playFortifyStage(any(Turn.class));
        assertEquals(gameDataDto, result);
    }

    @Test
    void playCurrentPhase_WithCardPhase_ShouldCallCardStage() throws Exception {
        turn.setTurnPhase(TurnPhase.CARD);
        setupPlayCurrentPhaseMocks();

        when(gameMapper.toGameData(any(Game.class))).thenReturn(gameDataDto);
        GameDataDto result = gameService.playCurrentPhase(gameId, gameDataDto);

        verify(mockTurnService).playCardStage(any(Turn.class));
        assertEquals(gameDataDto, result);
    }

    @Test
    void playCurrentPhase_WithBotPlayer_ShouldUseBotStrategy() throws Exception {
        player.setPlayerType(PlayerType.BOT);
        turn.setPlayer(player);
        setupPlayCurrentPhaseMocks();
        when(gameMapper.toGameData(any(Game.class))).thenReturn(gameDataDto);

        GameDataDto result = gameService.playCurrentPhase(gameId, gameDataDto);

        verify(turnStrategyFactory).getStrategy(PlayerType.BOT);
        assertEquals(gameDataDto, result);
    }

    @Test
    void playCurrentPhase_WithLoserPlayer_ShouldMarkAsLost() throws Exception {

        player.setTerritories(new ArrayList<>());
        game.setPlayers(List.of(player));
        setupPlayCurrentPhaseMocks();

        when(gameMapper.toGameData(any(Game.class))).thenReturn(gameDataDto);

        GameDataDto result = gameService.playCurrentPhase(gameId, gameDataDto);

        verify(playerPersistence).updatePlayer(eq(playerId), any(Player.class));
        assertEquals(gameDataDto, result);
    }

    @Test
    void nextPhase_ShouldUpdateTurnPhase() {
        when(persistence.getGameById(gameId)).thenReturn(game);
        when(modelMapper.map(any(), eq(Turn.class))).thenReturn(turn);
        when(turnServiceCommon.updateTurnPhase(any(Turn.class))).thenReturn(turn);
        when(gameMapper.toGameData(game)).thenReturn(gameDataDto);

        PlayerDto playerDto = new PlayerDto();
        playerDto.setPlayerId(playerId);
        playerDto.setTerritories(new ArrayList<>());
        gameDataDto.setPlayers(Arrays.asList(playerDto));

        GameDataDto result = gameService.nextPhase(gameId, gameDataDto);

        // Assert
        assertEquals(gameDataDto, result);
        verify(turnServiceCommon).updateTurnPhase(any(Turn.class));
    }

    // Helper methods
    private void setupStartGameMocks() {
        List<Territory> territories = Arrays.asList(new Territory(), new Territory());
        List<ObjectiveDto> objectives = new ArrayList<>();
        List<TerritoryState> territoryStates = Arrays.asList(new TerritoryState(), new TerritoryState());

        when(territoryPersistence.findAllTerritories()).thenReturn(territories);
        when(objectiveService.getAllByObjectiveTypes(any())).thenReturn(objectives);
        when(territoryStatePersistence.createTerritoryState(any(TerritoryState.class))).thenReturn(new TerritoryState());
        when(territoryStatePersistence.findTerritoryStateByPlayer(any())).thenReturn(Arrays.asList(new TerritoryState(), new TerritoryState()));
        when(territoryStatePersistence.updateTerritoryState(any(), any(TerritoryState.class))).thenReturn(new TerritoryState());
        when(persistence.updateGame(eq(gameId), any(Game.class))).thenReturn(game);
    }

    private void setupPlayCurrentPhaseMocks() {
        when(persistence.getGameById(gameId)).thenReturn(game);
        when(modelMapper.map(any(), eq(Turn.class))).thenReturn(turn);
        when(turnStrategyFactory.getStrategy(any(PlayerType.class))).thenReturn(mockTurnService);
        when(turnServiceCommon.checkObjectiveStates(any(Turn.class))).thenReturn(turn);

        PlayerDto playerDto = new PlayerDto();
        playerDto.setPlayerId(playerId);
        playerDto.setTerritories(new ArrayList<>());
        gameDataDto.setPlayers(Arrays.asList(playerDto));
    }
}