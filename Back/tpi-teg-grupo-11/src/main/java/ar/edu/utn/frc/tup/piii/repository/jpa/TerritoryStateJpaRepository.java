package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.TerritoryStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerritoryStateJpaRepository extends JpaRepository<TerritoryStateEntity, Long> {
    List<TerritoryStateEntity> findAllByPlayer_PlayerId(Long playerId);

    List<TerritoryStateEntity> findAllByTerritoryEntity_Region_RegionId(Long territoryEntityRegionRegionId);
}
