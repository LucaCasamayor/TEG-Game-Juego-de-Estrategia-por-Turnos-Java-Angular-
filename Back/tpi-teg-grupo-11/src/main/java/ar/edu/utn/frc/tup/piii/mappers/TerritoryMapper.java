package ar.edu.utn.frc.tup.piii.mappers;

import ar.edu.utn.frc.tup.piii.dtos.TerritoryDto;
import ar.edu.utn.frc.tup.piii.entities.TerritoryEntity;
import ar.edu.utn.frc.tup.piii.models.Territory;

public interface TerritoryMapper {
    Territory toModel(TerritoryEntity entity);
    Territory toModel(TerritoryDto dto);
    TerritoryDto toDto(Territory model);
    TerritoryEntity toEntity(Territory territory);
}
