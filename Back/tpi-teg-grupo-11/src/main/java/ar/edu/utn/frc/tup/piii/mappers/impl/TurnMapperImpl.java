package ar.edu.utn.frc.tup.piii.mappers.impl;

import ar.edu.utn.frc.tup.piii.dtos.MovementDto;
import ar.edu.utn.frc.tup.piii.dtos.TurnDto;
import ar.edu.utn.frc.tup.piii.entities.MovementEntity;
import ar.edu.utn.frc.tup.piii.entities.TurnEntity;
import ar.edu.utn.frc.tup.piii.mappers.PlayerMapper;
import ar.edu.utn.frc.tup.piii.mappers.TurnMapper;
import ar.edu.utn.frc.tup.piii.models.Movement;
import ar.edu.utn.frc.tup.piii.models.Turn;
import ar.edu.utn.frc.tup.piii.repository.jpa.GameJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TurnMapperImpl implements TurnMapper {

    private final PlayerMapper playerMapper;
    private final ModelMapper modelMapper;
    private final GameJpaRepository gameJpaRepository;

    @Override
    public Turn toModel(TurnEntity entity) {
        Turn model = new Turn();
        model.setGameId(entity.getGame().getGameId());
        model.setTurnId(entity.getTurnId());
        model.setPlayer(playerMapper.toModel(entity.getPlayer()));
        model.setTurnPhase(entity.getTurnPhase());
        if (entity.getMovements() != null) {
            model.setMovements(entity.getMovements()
                    .stream()
                    .map(x -> modelMapper.map(x, Movement.class))
                    .collect(Collectors.toList())
            );
        }
        return model;
    }

    @Override
    public Turn toModel(TurnDto dto) {
        Turn model = new Turn();
        model.setTurnId(dto.getTurnId());
        model.setPlayer(playerMapper.toModel(dto.getPlayer()));
        model.setTurnPhase(dto.getTurnPhase());
        if (model.getMovements() != null) {

            model.setMovements(dto.getMovements()
                    .stream()
                    .map(x -> modelMapper.map(x, Movement.class))
                    .collect(Collectors.toList())
            );
        }
        return model;
    }

    @Override
    public TurnDto toDto(Turn model) {
        TurnDto dto = new TurnDto();
        dto.setTurnId(model.getTurnId());
        dto.setPlayer(playerMapper.toDto(model.getPlayer()));
        dto.setTurnPhase(model.getTurnPhase());
        if (model.getMovements() != null) {
            dto.setMovements(model.getMovements()
                    .stream()
                    .map(x -> modelMapper.map(x, MovementDto.class))
                    .collect(Collectors.toList())
            );

        }
        return dto;
    }

    @Override
    public TurnEntity toEntity(Turn model) {
        TurnEntity entity = new TurnEntity();
        entity.setTurnId(model.getTurnId());
        entity.setPlayer(playerMapper.toEntity(model.getPlayer()));
        entity.setTurnPhase(model.getTurnPhase());
        entity.setGame(gameJpaRepository.getReferenceById(model.getGameId()));
        if (model.getMovements() != null) {

            entity.setMovements(model.getMovements()
                    .stream()
                    .map(x -> modelMapper.map(x, MovementEntity.class))
                    .collect(Collectors.toList())
            );
        }
        return entity;
    }

    @Override
    public TurnEntity toEntity(Turn model, UUID gameId) {
        TurnEntity entity = new TurnEntity();
        entity.setTurnId(model.getTurnId());
        entity.setPlayer(playerMapper.toEntity(model.getPlayer()));
        entity.setTurnPhase(model.getTurnPhase());
        entity.setGame(gameJpaRepository.getReferenceById(gameId));
        if (model.getMovements() != null) {

            entity.setMovements(model.getMovements()
                    .stream()
                    .map(x -> modelMapper.map(x, MovementEntity.class))
                    .collect(Collectors.toList())
            );
        }
        return entity;
    }
}
