package ar.edu.utn.frc.tup.piii.repository.jpa;


import ar.edu.utn.frc.tup.piii.entities.GlobalPactEntity;
import ar.edu.utn.frc.tup.piii.entities.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GlobalPactJpaRepository extends JpaRepository<GlobalPactEntity, Long> {
    List<GlobalPactEntity> findAllByPlayer1(PlayerEntity player);
    List<GlobalPactEntity> findAllByPlayer2(PlayerEntity player);
}
