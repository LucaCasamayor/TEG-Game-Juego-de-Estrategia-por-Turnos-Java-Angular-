package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.AchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AchievementJpaRepository extends JpaRepository<AchievementEntity, Long> {
    Optional<AchievementEntity> findByName(String name);

    Optional<AchievementEntity> findByAchievementId(Long achievementId);

    Long achievementId(Long achievementId);
}
