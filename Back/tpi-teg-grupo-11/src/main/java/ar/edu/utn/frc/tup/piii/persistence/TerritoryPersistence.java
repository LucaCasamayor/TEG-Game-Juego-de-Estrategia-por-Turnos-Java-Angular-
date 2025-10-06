package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.TerritoryEntity;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryMapper;
import ar.edu.utn.frc.tup.piii.models.Territory;
import ar.edu.utn.frc.tup.piii.repository.jpa.TerritoryJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TerritoryPersistence {

    private final TerritoryJpaRepository repository;
    private final ModelMapper modelMapper;
    private final TerritoryMapper territoryMapper;

    public Territory findById(Long id) {
        return modelMapper.map(repository.getReferenceById(id), Territory.class);
    }
    public List<Territory> findAllTerritories() {
        return repository.findAll().stream()
                .map(territoryMapper::toModel)
                .collect(Collectors.toList());
    }
    public List<Territory> findTerritoriesByRegion(Long id) {
        return repository.getAllByRegion_RegionId(id).stream()
                .map(territoryMapper::toModel)
                .collect(Collectors.toList());
    }
    public List<Territory> findTerritoriesByMap(Long id){
        return repository.getAllByRegion_Map_GameMapId(id)
                .stream()
                .map(territoryMapper::toModel)
                .collect(Collectors.toList());
    }
    public List<Territory> getBorderingTerritories(Long territoryId){
        return repository.findAllNeighbors(territoryId).stream()
                .map(territoryMapper::toModel)
                .collect(Collectors.toList());

    }
    public TerritoryEntity toEntityWithBorders(Territory model) {
        TerritoryEntity entity = territoryMapper.toEntity(model);

        if (model.getBordersId() != null) {
            List<TerritoryEntity> borders = model.getBordersId().stream()
                    .map(id -> repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Territory not found")))
                    .collect(Collectors.toList());
            entity.setBorders(borders);
        } else {
            entity.setBorders(List.of());
        }

        return entity;
    }
}