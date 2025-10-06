package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.TerritoryStateEntity;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryStateMapper;
import ar.edu.utn.frc.tup.piii.models.TerritoryState;
import ar.edu.utn.frc.tup.piii.repository.jpa.TerritoryStateJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TerritoryStatePersistence {

    private final TerritoryStateJpaRepository territoryStateJpaRepository;
    private final TerritoryStateMapper territoryStateMapper;

    public List<TerritoryState> findByRegionId(Long id){
        return territoryStateJpaRepository.findAllByTerritoryEntity_Region_RegionId(id)
                .stream().map(territoryStateMapper::toModel).collect(Collectors.toList());
    }

    public List<TerritoryState> findTerritoryStateByPlayer(Long playerId) {
        return territoryStateJpaRepository.findAllByPlayer_PlayerId(playerId)
                .stream()
                .map(territoryStateMapper::toModel)
                .collect(Collectors.toList());
    }

    public TerritoryState findById(Long id) {
        TerritoryStateEntity territoryState = territoryStateJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TerritoryState no encontrado"));

        return territoryStateMapper.toModel(territoryState);
    }

    public TerritoryState createTerritoryState(TerritoryState territoryState) {
        TerritoryStateEntity terStateEntity = territoryStateMapper.toEntity(territoryState);
        TerritoryStateEntity saved = territoryStateJpaRepository.save(terStateEntity);
        return territoryStateMapper.toModel(saved);
    }

    public TerritoryState updateTerritoryState(Long terStateId, TerritoryState territoryState) {
        TerritoryStateEntity prevTerStateEntity = territoryStateJpaRepository.getReferenceById(terStateId);
        TerritoryStateEntity terStateEnt = territoryStateMapper.toEntity(territoryState);

        prevTerStateEntity.setPlayer(terStateEnt.getPlayer());
        prevTerStateEntity.setArmyCount(terStateEnt.getArmyCount());

        return territoryStateMapper.toModel(territoryStateJpaRepository.save(prevTerStateEntity));
    }
}
