package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.dtos.PlayerDto;
import ar.edu.utn.frc.tup.piii.entities.GameEntity;
import ar.edu.utn.frc.tup.piii.entities.ObjectiveEntity;
import ar.edu.utn.frc.tup.piii.entities.PlayerEntity;
import ar.edu.utn.frc.tup.piii.entities.UserEntity;
import ar.edu.utn.frc.tup.piii.mappers.PlayerMapper;
import ar.edu.utn.frc.tup.piii.models.Player;
import ar.edu.utn.frc.tup.piii.models.TerritoryState;
import ar.edu.utn.frc.tup.piii.repository.jpa.GameJpaRepository;
import ar.edu.utn.frc.tup.piii.repository.jpa.PlayerJpaRepository;
import ar.edu.utn.frc.tup.piii.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerPersistence {

    private final PlayerJpaRepository playerJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final GameJpaRepository gameJpaRepository;
    private final PlayerMapper playerMapper;
    private final ModelMapper modelMapper;
    private final TerritoryStatePersistence territoryStatePersistence;

    public PlayerDto mapPlayer(Player player) {
        List<TerritoryState> territoryStates = territoryStatePersistence.findTerritoryStateByPlayer(player.getPlayerId());
        player.setTerritories(territoryStates);
        return playerMapper.toDto(player);
    }

    public Optional<Player> findByPlayer_PlayerId(Long playerId) {
        return playerJpaRepository.findById(playerId)
                .map(playerMapper::toModel);
    }
    public Player save(Player player) {
        PlayerEntity entity = playerMapper.toEntity(player);
        PlayerEntity savedEntity = playerJpaRepository.save(entity);
        return playerMapper.toModel(savedEntity);
    }



    public List<Player> findPlayersByGameId(UUID gameId) {
        return playerJpaRepository.getAllByGameId(gameId).stream()
                .map(playerMapper::toModel)
                .collect(Collectors.toList());
    }

    public Player createPlayer(Player player) {
        if (player.getGameId() == null) {
            throw new RuntimeException("El juego es requerido para crear un jugador");
        }

        UserEntity userEntity = userJpaRepository.findById(player.getUser().getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        GameEntity gameEntity = gameJpaRepository.findById(player.getGameId())
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));

        PlayerEntity playerEntity = playerMapper.toEntity(player);
        playerEntity.setUser(userEntity);
        playerEntity.setGameId(gameEntity.getGameId());
        if (player.getPlayerColor() == null) {
            playerEntity.setPlayerColor(player.getPlayerColor());
        }

        return playerMapper.toModel(playerJpaRepository.save(playerEntity));
    }

    public Player updatePlayer(Long playerId, Player player) {
        PlayerEntity existing = playerJpaRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));

        existing.setObjective(modelMapper.map(player.getObjective(), ObjectiveEntity.class));

        if (player.getPlayerColor() == null) {
            existing.setPlayerColor(player.getPlayerColor());
        }

        existing.setTurnOrder(player.getTurnOrder());
        existing.setWinner(player.isWinner());
        existing.setHasLost(player.isHasLost());
        existing.setBotDifficulty(player.getDifficulty());
        existing.setPlayerType(player.getPlayerType());

        return playerMapper.toModel(playerJpaRepository.save(existing));
    }

    public List<PlayerDto> findAll() {
        return playerJpaRepository.findAll().stream()
                .map(playerMapper::toModel)
                .map(playerMapper::toDto)
                .collect(Collectors.toList());
    }
}
