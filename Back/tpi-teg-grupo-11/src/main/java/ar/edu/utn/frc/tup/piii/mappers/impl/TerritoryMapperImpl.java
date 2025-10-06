package ar.edu.utn.frc.tup.piii.mappers.impl;

import ar.edu.utn.frc.tup.piii.dtos.RegionDto;
import ar.edu.utn.frc.tup.piii.dtos.TerritoryDto;
import ar.edu.utn.frc.tup.piii.entities.RegionEntity;
import ar.edu.utn.frc.tup.piii.entities.TerritoryEntity;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryMapper;
import ar.edu.utn.frc.tup.piii.models.Region;
import ar.edu.utn.frc.tup.piii.models.Territory;
import ar.edu.utn.frc.tup.piii.repository.jpa.TerritoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TerritoryMapperImpl implements TerritoryMapper {
    private final ModelMapper modelMapper;
    private final TerritoryJpaRepository territoryJpaRepository;


    @Override
    public Territory toModel(TerritoryEntity entity) {
        if(entity == null){
            return null;
        }

        Territory model = new Territory();
        model.setTerritoryId(entity.getTerritoryId());
        model.setRegion(modelMapper.map(entity.getRegion(), Region.class));
        model.setName(entity.getName());

        model.setBordersId(territoryJpaRepository.findAllNeighbors(entity.getTerritoryId())
                .stream().map(TerritoryEntity::getTerritoryId).collect(Collectors.toList()));
        return model;
    }

    @Override
    public Territory toModel(TerritoryDto dto) {
        if(dto == null){
            return null;
        }

        Territory model = new Territory();
        model.setTerritoryId(dto.getTerritoryId());
        model.setRegion(modelMapper.map(dto.getRegion(), Region.class));
        model.setName(dto.getName());

        model.setBordersId(dto.getBordersId());
        return model;
    }

    @Override
    public TerritoryDto toDto(Territory model) {
        if(model == null){
            return null;
        }

        TerritoryDto dto = new TerritoryDto();
        dto.setTerritoryId(model.getTerritoryId());
        dto.setRegion(modelMapper.map(model.getRegion(), RegionDto.class));
        dto.setName(model.getName());
        dto.setBordersId(model.getBordersId());
        return dto;
    }

    @Override
    public TerritoryEntity toEntity(Territory model) {
        if (model == null) {
            return null;
        }

        TerritoryEntity entity = new TerritoryEntity();
        entity.setTerritoryId(model.getTerritoryId());
        entity.setName(model.getName());
        entity.setRegion(modelMapper.map(model.getRegion(), RegionEntity.class));

        return entity;
    }
}
