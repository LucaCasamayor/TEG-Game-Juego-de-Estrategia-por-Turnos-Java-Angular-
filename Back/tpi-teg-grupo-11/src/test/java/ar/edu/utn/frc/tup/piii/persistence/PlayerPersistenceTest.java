package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.dtos.PlayerDto;
import ar.edu.utn.frc.tup.piii.entities.*;
import ar.edu.utn.frc.tup.piii.enums.BotDifficulty;
import ar.edu.utn.frc.tup.piii.enums.PlayerType;
import ar.edu.utn.frc.tup.piii.mappers.PlayerMapper;
import ar.edu.utn.frc.tup.piii.models.*;
import ar.edu.utn.frc.tup.piii.repository.jpa.GameJpaRepository;
import ar.edu.utn.frc.tup.piii.repository.jpa.PlayerJpaRepository;
import ar.edu.utn.frc.tup.piii.repository.jpa.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerPersistenceTest {

    private PlayerJpaRepository playerRepo;
    private UserJpaRepository userRepo;
    private GameJpaRepository gameRepo;
    private PlayerMapper playerMapper;
    private ModelMapper modelMapper;
    private TerritoryStatePersistence territoryStatePersistence;

    private PlayerPersistence persistence;

    @BeforeEach
    void setup() {
        playerRepo = mock(PlayerJpaRepository.class);
        userRepo = mock(UserJpaRepository.class);
        gameRepo = mock(GameJpaRepository.class);
        playerMapper = mock(PlayerMapper.class);
        modelMapper = mock(ModelMapper.class);
        territoryStatePersistence = mock(TerritoryStatePersistence.class);
        persistence = new PlayerPersistence(playerRepo, userRepo, gameRepo, playerMapper, modelMapper, territoryStatePersistence);
    }

    @Test
    void mapPlayer_withTerritories_returnsPlayerDto() {
        Player player = new Player();
        player.setPlayerId(1L);

        TerritoryState t = new TerritoryState();
        when(territoryStatePersistence.findTerritoryStateByPlayer(1L)).thenReturn(List.of(t));
        PlayerDto dto = new PlayerDto();
        when(playerMapper.toDto(player)).thenReturn(dto);

        PlayerDto result = persistence.mapPlayer(player);

        assertEquals(dto, result);
        assertEquals(1, player.getTerritories().size());
    }

    @Test
    void findByPlayer_PlayerId_existingId_returnsPlayer() {
        PlayerEntity entity = new PlayerEntity();
        Player player = new Player();

        when(playerRepo.findById(1L)).thenReturn(Optional.of(entity));
        when(playerMapper.toModel(entity)).thenReturn(player);

        Optional<Player> result = persistence.findByPlayer_PlayerId(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void findByPlayer_PlayerId_nonExistingId_returnsEmpty() {
        when(playerRepo.findById(1L)).thenReturn(Optional.empty());

        Optional<Player> result = persistence.findByPlayer_PlayerId(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void save_validPlayer_returnsSavedPlayer() {
        Player player = new Player();
        PlayerEntity entity = new PlayerEntity();

        when(playerMapper.toEntity(player)).thenReturn(entity);
        when(playerRepo.save(entity)).thenReturn(entity);
        when(playerMapper.toModel(entity)).thenReturn(player);

        Player result = persistence.save(player);

        assertEquals(player, result);
    }

    @Test
    void findPlayersByGameId_validId_returnsList() {
        PlayerEntity entity = new PlayerEntity();
        Player player = new Player();
        UUID gameId = UUID.randomUUID();

        when(playerRepo.getAllByGameId(gameId)).thenReturn(List.of(entity));
        when(playerMapper.toModel(entity)).thenReturn(player);

        List<Player> result = persistence.findPlayersByGameId(gameId);

        assertEquals(1, result.size());
    }

    @Test
    void createPlayer_validPlayer_returnsCreatedPlayer() {
        Player player = new Player();
        UUID userId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();

        User user = new User();
        user.setUserId(userId);
        player.setUser(user);
        player.setGameId(gameId);

        UserEntity userEntity = new UserEntity();
        GameEntity gameEntity = new GameEntity();
        gameEntity.setGameId(gameId);

        PlayerEntity playerEntity = new PlayerEntity();
        Player resultModel = new Player();

        when(userRepo.findById(userId)).thenReturn(Optional.of(userEntity));
        when(gameRepo.findById(gameId)).thenReturn(Optional.of(gameEntity));
        when(playerMapper.toEntity(player)).thenReturn(playerEntity);
        when(playerRepo.save(playerEntity)).thenReturn(playerEntity);
        when(playerMapper.toModel(playerEntity)).thenReturn(resultModel);

        Player result = persistence.createPlayer(player);

        assertNotNull(result);
    }

    @Test
    void createPlayer_missingUser_throwsException() {
        Player player = new Player();
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setUserId(userId);
        player.setUser(user);
        player.setGameId(UUID.randomUUID());

        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> persistence.createPlayer(player));
    }

    @Test
    void createPlayer_missingGame_throwsException() {
        Player player = new Player();
        UUID userId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();

        User user = new User();
        user.setUserId(userId);
        player.setUser(user);
        player.setGameId(gameId);

        when(userRepo.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(gameRepo.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> persistence.createPlayer(player));
    }

    @Test
    void updatePlayer_existingPlayer_updatesAndReturns() {
        Long id = 1L;
        Player player = new Player();
        player.setObjective(new Objective());
        player.setPlayerColor(null); // Para testear ese if
        player.setTurnOrder(1);
        player.setWinner(true);
        player.setHasLost(false);
        player.setDifficulty(BotDifficulty.HARD);
        player.setPlayerType(PlayerType.BOT);

        PlayerEntity entity = new PlayerEntity();
        ObjectiveEntity objectiveEntity = new ObjectiveEntity();
        when(playerRepo.findById(id)).thenReturn(Optional.of(entity));
        when(modelMapper.map(player.getObjective(), ObjectiveEntity.class)).thenReturn(objectiveEntity);
        when(playerRepo.save(entity)).thenReturn(entity);
        when(playerMapper.toModel(entity)).thenReturn(player);

        Player result = persistence.updatePlayer(id, player);

        assertEquals(player, result);
    }

    @Test
    void updatePlayer_notFound_throwsException() {
        when(playerRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> persistence.updatePlayer(1L, new Player()));
    }

    @Test
    void findAll_returnsListOfPlayerDtos() {
        PlayerEntity entity = new PlayerEntity();
        Player model = new Player();
        PlayerDto dto = new PlayerDto();

        when(playerRepo.findAll()).thenReturn(List.of(entity));
        when(playerMapper.toModel(entity)).thenReturn(model);
        when(playerMapper.toDto(model)).thenReturn(dto);

        List<PlayerDto> result = persistence.findAll();

        assertEquals(1, result.size());
    }
}
