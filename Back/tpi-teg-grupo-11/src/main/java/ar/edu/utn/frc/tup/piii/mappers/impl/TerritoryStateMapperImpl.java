package ar.edu.utn.frc.tup.piii.mappers.impl;

import ar.edu.utn.frc.tup.piii.dtos.TerritoryStateDto;
import ar.edu.utn.frc.tup.piii.entities.TerritoryStateEntity;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryMapper;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryStateMapper;
import ar.edu.utn.frc.tup.piii.models.TerritoryState;
import ar.edu.utn.frc.tup.piii.persistence.TerritoryPersistence;
import ar.edu.utn.frc.tup.piii.repository.jpa.PlayerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TerritoryStateMapperImpl implements TerritoryStateMapper {
    private final TerritoryMapper territoryMapper;
    private final TerritoryPersistence territoryPersistence;
    private final PlayerJpaRepository playerJpaRepository;

    @Override
    public TerritoryState toModel(TerritoryStateEntity entity) {
        if(entity == null) {
            return null;
        }

        TerritoryState model = new TerritoryState();
        model.setTerritoryStateId(entity.getTerritoryStateId());
        model.setPlayerId(entity.getPlayer().getPlayerId());
        model.setArmyCount(entity.getArmyCount());
        model.setTerritory(territoryMapper.toModel(entity.getTerritoryEntity()));
        // model.setReinforcements(territoryMapper.toModel(entity.getReinforcements()));

        return model;
    }

    @Override
    public TerritoryState toModel(TerritoryStateDto dto) {
        if(dto == null) {
            return null;
        }

        TerritoryState model = new TerritoryState();
        model.setTerritoryStateId(dto.getTerritoryStateId());
        model.setArmyCount(dto.getArmyCount());
        model.setTerritory(territoryMapper.toModel(dto.getTerritory()));
        model.setPlayerId(dto.getPlayer());
        // model.setReinforcements(territoryMapper.toModel(entity.getReinforcements()));

        return model;
    }

    @Override
    public TerritoryStateEntity toEntity(TerritoryState model) {
        if (model == null) {
            return null;
        }

        TerritoryStateEntity entity = new TerritoryStateEntity();
        entity.setTerritoryStateId(model.getTerritoryStateId());
        entity.setArmyCount(model.getArmyCount());

        entity.setPlayer(playerJpaRepository.getReferenceById(model.getPlayerId()));
        // entity.setReinforcements(territoryMapper.toEntity(model.getReinforcements()));

        entity.setTerritoryEntity(territoryPersistence.toEntityWithBorders(model.getTerritory()));

        return entity;
    }
}
