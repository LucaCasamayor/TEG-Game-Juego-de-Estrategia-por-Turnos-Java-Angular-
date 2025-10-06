package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.PactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PactJpaRepository extends JpaRepository<PactEntity, Long> {

    List<PactEntity> getAllByTerritory1_TerritoryStateId(Long id);

    List<PactEntity> getAllByTerritory2_TerritoryStateId(Long id);

    List<PactEntity> getAllByGame_GameId(UUID gameId);
}
