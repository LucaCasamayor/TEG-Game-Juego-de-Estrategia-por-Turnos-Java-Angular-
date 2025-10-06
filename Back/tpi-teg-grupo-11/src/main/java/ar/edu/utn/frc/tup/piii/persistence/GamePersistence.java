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
import ar.edu.utn.frc.tup.piii.repository.jpa.GameJpaRepository;
import ar.edu.utn.frc.tup.piii.repository.jpa.SettingsJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GamePersistence {

    private final GameJpaRepository gameJpaRepository;
    private final SettingsJpaRepository settingsJpaRepository;
    private final GameMapper gameMapper;
    private final PlayerMapper playerMapper;
    private final SettingsMapper settingsMapper;
    private final TurnMapper turnMapper;


    public GameDataDto findGameDataById(UUID gameId) {
        GameEntity game = gameJpaRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Juego no encontrado"));

        Game model = gameMapper.toModel(game);
        return gameMapper.toGameData(model);
    }

    public GameDto findGameById(UUID gameId) {
        GameEntity game = gameJpaRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Juego no encontrado"));

        Game model = gameMapper.toModel(game);

        GameDto dto = new GameDto();
        dto.setGameId(model.getGameId());
        dto.setPlayers(model.getPlayers().stream()
                .map(playerMapper::toDto)
                .collect(Collectors.toList()));
        dto.setSettings(settingsMapper.toDto(model.getSettings()));
        dto.setCreationDate(model.getCreationDate());
        dto.setGameState(model.getGameState());

        return dto;
    }

    public Game createGame(Game game) {
        SettingsEntity settingsEntity = settingsJpaRepository.findById(game.getSettings().getSettingsId())
                .orElseThrow(() -> new EntityNotFoundException("Settings no encontrado"));

        GameEntity gameEntity = gameMapper.toEntity(game);
        gameEntity.setSettings(settingsEntity);

        List<PlayerEntity> playerEntities = game.getPlayers().stream()
                .map(player -> {
                    PlayerEntity playerEntity = playerMapper.toEntity(player);
                    playerEntity.setGameId(gameEntity.getGameId());
                    return playerEntity;
                }).collect(Collectors.toList());

        gameEntity.setPlayers(playerEntities);

        GameEntity savedEntity = gameJpaRepository.save(gameEntity);

        return gameMapper.toModel(savedEntity);
    }

    public Game updateGame(UUID gameId, Game game) {
        GameEntity existing = gameJpaRepository.findByGameId(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Juego no encontrado"));

        existing.setGameState(game.getGameState());

        return gameMapper.toModel(gameJpaRepository.save(existing));
    }

    public List<Game> findGameLobbies() {
        return gameJpaRepository.findByGameState(GameState.WAITING_PLAYERS)
                .stream()
                .map(gameMapper::toModel)
                .collect(Collectors.toList());
    }

    public Game getGameById(UUID gameId) {
        GameEntity gameEntity = gameJpaRepository.findByGameId(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Juego no encontrado"));
        return gameMapper.toModel(gameEntity);
    }

    public Game joinGame(UUID gameId, Player player, String password) {
        GameEntity gameEntity = gameJpaRepository.findByGameId(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Juego no encontrado"));

        Game game = gameMapper.toModel(gameEntity);

        // Validaciones
        if (game.getPlayers().stream().anyMatch(p -> p.getPlayerId().equals(player.getPlayerId()))) {
            throw new IllegalArgumentException("El jugador ya está en la partida");
        }

        if(!game.getGameState().equals(GameState.WAITING_PLAYERS)) {
            throw new IllegalArgumentException("Ya no te puedes unir");
        }

        if (game.getPlayers().size() == 6) {
            throw new IllegalStateException("La partida está llena");
        }

        if (game.getSettings().isPrivate()) {
            if (!game.getSettings().getPassword().equals(password)) {
                throw new SecurityException("Contraseña incorrecta");
            }
        }

        // Agregar jugador
        game.getPlayers().add(player);

        // Mapear modelo actualizado a entidad
        GameEntity updatedEntity = gameMapper.toEntity(game);

        // Mapear jugadores a entidades y setear el gameId
        List<PlayerEntity> playerEntities = game.getPlayers().stream()
                .map(p -> {
                    PlayerEntity pe = playerMapper.toEntity(p);
                    pe.setGameId(game.getGameId());
                    return pe;
                }).collect(Collectors.toList());

        updatedEntity.setPlayers(playerEntities);

        // Persistir
        GameEntity savedEntity = gameJpaRepository.save(updatedEntity);
        return gameMapper.toModel(savedEntity);
    }
}
