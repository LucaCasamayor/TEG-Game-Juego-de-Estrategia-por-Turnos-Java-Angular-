package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.ObjectiveEntity;
import ar.edu.utn.frc.tup.piii.entities.ObjectiveTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveJpaRepository extends JpaRepository<ObjectiveEntity, Long> {

    List<ObjectiveEntity> getAllByObjectiveType(ObjectiveTypeEntity objetiveType);
}
