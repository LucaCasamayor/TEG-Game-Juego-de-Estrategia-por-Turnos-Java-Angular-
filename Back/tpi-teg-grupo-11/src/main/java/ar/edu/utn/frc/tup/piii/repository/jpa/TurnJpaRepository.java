package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.TurnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TurnJpaRepository extends JpaRepository<TurnEntity, Long> {
    List<TurnEntity> getAllByGame_GameId(UUID gameId);

    List<TurnEntity> findByPlayer_PlayerId(Long playerPlayerId);

    List<TurnEntity> findAllByGame_GameId(UUID gameGameId);
}