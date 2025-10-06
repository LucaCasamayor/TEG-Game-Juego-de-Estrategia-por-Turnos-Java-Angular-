package ar.edu.utn.frc.tup.piii.mappers.impl;

import ar.edu.utn.frc.tup.piii.entities.DiceEntity;
import ar.edu.utn.frc.tup.piii.entities.MovementEntity;
import ar.edu.utn.frc.tup.piii.mappers.MovementMapper;
import ar.edu.utn.frc.tup.piii.models.Movement;
import ar.edu.utn.frc.tup.piii.repository.jpa.TerritoryStateJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovementMapperImpl implements MovementMapper {

    private final ModelMapper modelMapper;
    private final TerritoryStateJpaRepository territoryStateJpaRepository;

    @Override
    public MovementEntity toEntity(Movement model) {
        MovementEntity entity = new MovementEntity();

        entity.setId(model.getId());
        if(model.getMovementType() != null) {
            entity.setMovementType(model.getMovementType());
        }
        if(model.getStartTerritoryId() != null) {
            entity.setStartTerritory(territoryStateJpaRepository.findById(model.getStartTerritoryId())
                    .orElseThrow(() -> new EntityNotFoundException("TerritoryState Start not found")));
        }
        if(model.getEndTerritoryId() != null) {
            entity.setEndTerritory(territoryStateJpaRepository.findById(model.getEndTerritoryId())
                    .orElseThrow(() -> new EntityNotFoundException("TerritoryState End not found")));
        }
        if(model.getDice() != null) {
            entity.setDice(model.getDice()
                    .stream().map(d -> modelMapper.map(d, DiceEntity.class)).collect(Collectors.toList()));
        }
        entity.setArmyCount(model.getArmyCount());

        return entity;
    }
}
