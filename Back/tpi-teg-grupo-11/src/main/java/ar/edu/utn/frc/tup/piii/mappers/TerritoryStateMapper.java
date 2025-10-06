package ar.edu.utn.frc.tup.piii.mappers;

import ar.edu.utn.frc.tup.piii.dtos.TerritoryStateDto;
import ar.edu.utn.frc.tup.piii.entities.TerritoryStateEntity;
import ar.edu.utn.frc.tup.piii.models.TerritoryState;

public interface TerritoryStateMapper {
    TerritoryState toModel(TerritoryStateEntity entity);

    TerritoryState toModel(TerritoryStateDto dto);

    TerritoryStateEntity toEntity(TerritoryState model);
}
