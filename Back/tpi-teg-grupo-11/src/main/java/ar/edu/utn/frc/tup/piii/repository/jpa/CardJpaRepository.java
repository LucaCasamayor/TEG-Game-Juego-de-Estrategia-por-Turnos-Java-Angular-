package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardJpaRepository extends JpaRepository<CardEntity, Long> {
    List<CardEntity> getAllByTerritory_Region_Map_GameMapId(Long territoryRegionMapGameMapId);
    CardEntity getCardEntityByTerritory_TerritoryId(Long territoryTerritoryId);
}
