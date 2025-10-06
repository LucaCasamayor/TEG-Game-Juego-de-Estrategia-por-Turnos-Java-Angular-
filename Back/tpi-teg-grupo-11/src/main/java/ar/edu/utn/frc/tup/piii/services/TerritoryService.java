package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.TerritoryDto;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryMapper;
import ar.edu.utn.frc.tup.piii.models.Territory;
import ar.edu.utn.frc.tup.piii.persistence.TerritoryPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TerritoryService {

    private final TerritoryPersistence persistence;
    private final TerritoryMapper territoryMapper;

    public List<TerritoryDto> getAllByGameId(Long mapId) {
        return persistence.findTerritoriesByMap(mapId).stream()
                .map(territoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TerritoryDto> getAllByRegionId(Long regionId) {
        return persistence.findTerritoriesByRegion(regionId).stream()
                .map(territoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public TerritoryDto getById(Long id){
        return territoryMapper.toDto(persistence.findById(id));
    }

    public List<Territory> getBorderingCountries(Long territoryId){
        return persistence.getBorderingTerritories(territoryId);
    }
}