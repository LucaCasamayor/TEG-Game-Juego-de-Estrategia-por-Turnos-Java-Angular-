package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.DiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiceJpaRepository extends JpaRepository<DiceEntity, Long> {
   // List<DiceEntity> findAllByMovement(MovementEntity movement);

}
