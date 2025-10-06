package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.PlayerGameStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerGameStatsJpaRepository extends JpaRepository<PlayerGameStatsEntity, Long> {

}
