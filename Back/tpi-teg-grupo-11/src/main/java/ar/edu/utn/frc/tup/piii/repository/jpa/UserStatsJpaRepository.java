package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.UserStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserStatsJpaRepository extends JpaRepository<UserStatsEntity, Long> {
    List<UserStatsEntity> findAll();
    UserStatsEntity save(UserStatsEntity stats);
}

