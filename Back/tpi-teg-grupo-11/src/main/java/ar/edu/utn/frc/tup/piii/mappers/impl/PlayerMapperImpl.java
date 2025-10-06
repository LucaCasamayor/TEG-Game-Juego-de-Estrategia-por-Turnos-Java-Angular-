package ar.edu.utn.frc.tup.piii.mappers.impl;

import ar.edu.utn.frc.tup.piii.dtos.ObjectiveDto;
import ar.edu.utn.frc.tup.piii.dtos.PlayerDto;
import ar.edu.utn.frc.tup.piii.dtos.TerritoryStateDto;
import ar.edu.utn.frc.tup.piii.dtos.UserDto;
import ar.edu.utn.frc.tup.piii.entities.ObjectiveEntity;
import ar.edu.utn.frc.tup.piii.entities.PlayerEntity;
import ar.edu.utn.frc.tup.piii.entities.UserEntity;
import ar.edu.utn.frc.tup.piii.mappers.PlayerMapper;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryStateMapper;
import ar.edu.utn.frc.tup.piii.models.Objective;
import ar.edu.utn.frc.tup.piii.models.Player;
import ar.edu.utn.frc.tup.piii.models.User;
import ar.edu.utn.frc.tup.piii.repository.jpa.TerritoryStateJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlayerMapperImpl implements PlayerMapper {
    private final ModelMapper modelMapper;
    private final TerritoryStateJpaRepository territoryStateJpaRepository;
    private final TerritoryStateMapper territoryStateMapper;

    @Override
    public Player toModel(PlayerDto dto) {
        if (dto == null) throw new IllegalArgumentException("Player source cannot be null");

        Player player = new Player();
        player.setPlayerId(dto.getPlayerId());
        player.setPlayerColor(dto.getPlayerColor());
        player.setTurnOrder(dto.getTurnOrder());
        player.setHasLost(dto.isHasLost());
        player.setPlayerType(dto.getPlayerType());
        player.setWinner(dto.isWinner());
        player.setDifficulty(dto.getDifficulty());
//        player.setTerritories(dto.getTerritories().stream().map(territoryStateMapper::toModel).collect(Collectors.toList()));
        if (dto.getUser() != null) {
            player.setUser(modelMapper.map(dto.getUser(), User.class));
        }

        if (dto.getObjective() != null) {
            player.setObjective(modelMapper.map(dto.getObjective(), Objective.class));
        }

        if (dto.getGameId() != null) {
            player.setGameId(dto.getGameId());
        }

        return player;
    }

    @Override
    public Player toModel(PlayerEntity entity) {
        if (entity == null) throw new IllegalArgumentException("Player source cannot be null");

        Player player = new Player();
        player.setPlayerId(entity.getPlayerId());
        player.setPlayerColor(entity.getPlayerColor());
        player.setTurnOrder(entity.getTurnOrder());
        player.setHasLost(entity.isHasLost());
        player.setPlayerType(entity.getPlayerType());
        player.setWinner(entity.isWinner());
        player.setDifficulty(entity.getBotDifficulty());

        if (entity.getUser() != null) {
            player.setUser(modelMapper.map(entity.getUser(), User.class));
        }

        if (entity.getObjective() != null) {
            player.setObjective(modelMapper.map(entity.getObjective(), Objective.class));
        }

        if (entity.getGameId() != null) {
            player.setGameId(entity.getGameId());
        }
        player.setTerritories(territoryStateJpaRepository.findAllByPlayer_PlayerId(entity.getPlayerId())
                .stream().map(territoryStateMapper::toModel).collect(Collectors.toList()));

        return player;
    }


    @Override
    public PlayerEntity toEntity(Player model) {
        if (model == null) throw new IllegalArgumentException("Player source cannot be null");

        PlayerEntity player = new PlayerEntity();
        player.setPlayerId(model.getPlayerId());
        player.setPlayerColor(model.getPlayerColor());
        player.setTurnOrder(model.getTurnOrder());
        player.setHasLost(model.isHasLost());
        player.setPlayerType(model.getPlayerType());
        player.setWinner(model.isWinner());
        player.setBotDifficulty(model.getDifficulty());
        player.setGameId(model.getGameId());


        if (model.getUser() != null) {
            player.setUser(modelMapper.map(model.getUser(), UserEntity.class));
        }

        if (model.getObjective() != null) {
            player.setObjective(modelMapper.map(model.getObjective(), ObjectiveEntity.class));
        }

        return player;
    }

    @Override
    public PlayerDto toDto(Player model) {
        if (model == null) throw new IllegalArgumentException("Player source cannot be null");

        PlayerDto dto = new PlayerDto();
        dto.setPlayerId(model.getPlayerId());
        dto.setPlayerColor(model.getPlayerColor());
        dto.setTurnOrder(model.getTurnOrder());
        dto.setHasLost(model.isHasLost());
        dto.setPlayerType(model.getPlayerType());
        dto.setWinner(model.isWinner());
        dto.setDifficulty(model.getDifficulty());
        if(model.getTerritories() != null) {
            dto.setTerritories(model.getTerritories()
                    .stream()
                    .map(territory -> modelMapper.map(territory, TerritoryStateDto.class))
                    .collect(Collectors.toList()));
        }

        if(model.getTerritories() != null) {
            dto.setTerritories(model.getTerritories()
                    .stream()
                    .map(territory -> modelMapper.map(territory, TerritoryStateDto.class))
                    .collect(Collectors.toList()));
        }


        // Mapear user a userDto si es necesario
        if (model.getUser() != null) {
            dto.setUser(modelMapper.map(model.getUser(), UserDto.class));
        }
        if (model.getUser() == null) {
            throw new IllegalArgumentException("Player sin usuario no puede ser mapeado");
        }

        // Mapear objective a objectiveDto si es necesario
        if (model.getObjective() != null) {
            dto.setObjective(modelMapper.map(model.getObjective(), ObjectiveDto.class));

        }
        dto.setGameId(model.getGameId());


        return dto;
    }
}
