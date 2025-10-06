package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.TerritoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerritoryJpaRepository extends JpaRepository<TerritoryEntity, Long> {

    List<TerritoryEntity> getAllByRegion_RegionId(Long id);

    List<TerritoryEntity> getAllByRegion_Map_GameMapId(Long regionMapGameMapId);

    @Query("""
        SELECT DISTINCT t
        FROM TerritoryEntity t
        WHERE t IN (
            SELECT b FROM TerritoryEntity t2 JOIN t2.borders b WHERE t2.territoryId = :territoryId
        )
        OR t.territoryId IN (
            SELECT t3.territoryId
            FROM TerritoryEntity t3 JOIN t3.borders b2
            WHERE b2.territoryId = :territoryId
        )
    """)
    List<TerritoryEntity> findAllNeighbors(@Param("territoryId") Long territoryId);
}
