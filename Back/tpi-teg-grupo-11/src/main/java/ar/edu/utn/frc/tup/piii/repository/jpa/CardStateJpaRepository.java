package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.CardStateEntity;
import ar.edu.utn.frc.tup.piii.models.CardState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface CardStateJpaRepository extends JpaRepository<CardStateEntity, Long> {

    List<CardStateEntity> getAllByGame_GameId(UUID gameId);

    List<CardStateEntity> findCardStateByPlayer_PlayerId(Long playerId);

    List<CardStateEntity> findByPlayerIsNull();
}
