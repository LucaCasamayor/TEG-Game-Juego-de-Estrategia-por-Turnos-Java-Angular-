package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.GameMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameMapJpaRepository extends JpaRepository<GameMapEntity, Long>{
    Optional<GameMapEntity> findByGameMapId(Long gameMapId);
}
