package ar.edu.utn.frc.tup.piii.mappers.impl;

import ar.edu.utn.frc.tup.piii.dtos.GameDataDto;
import ar.edu.utn.frc.tup.piii.dtos.GameDto;
import ar.edu.utn.frc.tup.piii.entities.GameEntity;
import ar.edu.utn.frc.tup.piii.entities.PlayerEntity;
import ar.edu.utn.frc.tup.piii.mappers.*;
import ar.edu.utn.frc.tup.piii.models.Game;
import ar.edu.utn.frc.tup.piii.models.Player;
import ar.edu.utn.frc.tup.piii.models.Turn;
import ar.edu.utn.frc.tup.piii.persistence.PlayerPersistence;
import ar.edu.utn.frc.tup.piii.persistence.TerritoryPersistence;
import ar.edu.utn.frc.tup.piii.repository.jpa.TurnJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GameMapperImpl implements GameMapper {

    private final SettingsMapper settingsMapper;
    private final PlayerMapper playerMapper;
    private final TurnMapper turnMapper;
    private final TerritoryPersistence territoryPersistence;
    private final PlayerPersistence playerPersistence;
    private final TerritoryMapper territoryMapper;
    private final TurnJpaRepository turnJpaRepository;

    @Override
    public Game toModel(GameEntity entity) {
        if (entity == null) {
            return null;
        }

        Game game = new Game();
        game.setGameId(entity.getGameId());
        game.setGameState(entity.getGameState());
        game.setCreationDate(entity.getCreationDate());
        game.setTurns(turnJpaRepository.findAllByGame_GameId(entity.getGameId())
                .stream().map(turnMapper::toModel).collect(Collectors.toList()));

        game.setSettings(settingsMapper.toModel(entity.getSettings()));

        List<PlayerEntity> players = entity.getPlayers();
        List<Player> playersModel = players.stream()
                .map(playerMapper::toModel)
                .toList();

        game.setPlayers(playersModel);

        return game;
    }

    @Override
    public GameEntity toEntity(Game model) {
        if (model == null) {
            return null;
        }

        GameEntity entity = new GameEntity();
        entity.setGameId(model.getGameId());
        entity.setGameState(model.getGameState());
        entity.setCreationDate(model.getCreationDate());

        return entity;
    }

    @Override
    public GameDto toDto(Game model) {
        if (model == null) {
            return null;
        }

        GameDto dto = new GameDto();
        dto.setGameId(model.getGameId());
        dto.setPlayers(model.getPlayers().stream().map(playerMapper::toDto).collect(Collectors.toList()));
        dto.setGameState(model.getGameState());
        dto.setCreationDate(model.getCreationDate());

        return dto;
    }

    @Override
    public GameDataDto toGameData(Game game) {
        List<Turn> turns = game.getTurns();
        Turn lastTurn = turns.get(turns.size() - 1);
        GameDataDto gameData = new GameDataDto();
        gameData.setTurns(game.getTurns().stream().map(turnMapper::toDto).collect(Collectors.toList()));
        gameData.setGameId(game.getGameId());
        gameData.setGameState(game.getGameState());

        gameData.setPlayers(
                game.getPlayers().stream()
                        .map(playerPersistence::mapPlayer)
                        .collect(Collectors.toList())
        );

        gameData.setCurrentTurn(turnMapper.toDto(lastTurn));
        if(game.getTurns().isEmpty()) {
            gameData.setCurrentTurnIndex(0);
        } else {
            int nextTurnOrder = 0;
            int prevTurnOrder = game.getTurns().get(game.getTurns().size()-1).getPlayer().getTurnOrder();
            if(prevTurnOrder < game.getPlayers().size()-1) {
                nextTurnOrder = prevTurnOrder;
            }
            gameData.setCurrentTurnIndex(nextTurnOrder);
        }
        gameData.setTurns(game.getTurns().stream().map(turnMapper::toDto).collect(Collectors.toList()));

        gameData.setTerritories(territoryPersistence.findAllTerritories()
                .stream().map(territoryMapper::toDto)
                .collect(Collectors.toList()));

        return gameData;
    }

}