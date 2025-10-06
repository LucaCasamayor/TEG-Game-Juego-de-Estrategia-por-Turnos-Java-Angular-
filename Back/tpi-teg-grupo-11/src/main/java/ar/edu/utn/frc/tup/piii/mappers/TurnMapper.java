package ar.edu.utn.frc.tup.piii.mappers;

import ar.edu.utn.frc.tup.piii.dtos.TurnDto;
import ar.edu.utn.frc.tup.piii.entities.TurnEntity;
import ar.edu.utn.frc.tup.piii.models.Turn;

import java.util.UUID;

public interface TurnMapper {
    Turn toModel(TurnEntity entity);
    Turn toModel(TurnDto dto);
    TurnDto toDto(Turn model);
    TurnEntity toEntity(Turn model);

    TurnEntity toEntity(Turn model, UUID gameId);
}
