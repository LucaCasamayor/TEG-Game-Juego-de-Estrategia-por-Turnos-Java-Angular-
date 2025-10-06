package ar.edu.utn.frc.tup.piii.persistence;


import ar.edu.utn.frc.tup.piii.entities.MovementEntity;
import ar.edu.utn.frc.tup.piii.entities.TurnEntity;
import ar.edu.utn.frc.tup.piii.mappers.GameMapper;
import ar.edu.utn.frc.tup.piii.mappers.MovementMapper;
import ar.edu.utn.frc.tup.piii.mappers.TurnMapper;
import ar.edu.utn.frc.tup.piii.models.Turn;
import ar.edu.utn.frc.tup.piii.repository.jpa.TurnJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TurnPersistence {

    private final TurnJpaRepository repository;
    private final ModelMapper modelMapper;
    private final TurnMapper turnMapper;
    private final MovementMapper movementMapper;

    public Turn saveTurn(Turn turn) {
        TurnEntity entity = modelMapper.map(turn, TurnEntity.class);
        return modelMapper.map(repository.save(entity), Turn.class);
    }

    public Turn findById(Long turnId) {
        return modelMapper.map(repository.getReferenceById(turnId), Turn.class);
    }

    public List<Turn> findByGame(UUID gameId) {
        List<TurnEntity> turns = repository.getAllByGame_GameId(gameId);
        return turns.stream()
                .map(turn -> modelMapper.map(turn, Turn.class))
                .collect(Collectors.toList());
    }

    public Turn updateTurn(Long id, Turn turn) {
        TurnEntity updTurn = repository.getReferenceById(id);
        updTurn.setTurnPhase(turn.getTurnPhase());
        updTurn.setMovements(turn.getMovements()
                .stream()
                // TODO error de mapeo aca
                .map(movementMapper::toEntity)
                .collect(Collectors.toList()));
        return turnMapper.toModel(repository.save(updTurn));
    }

    public List<Turn> findByPlayer(Long playerId) {
        List<TurnEntity> turnsEntities = repository.findByPlayer_PlayerId(playerId);

        return turnsEntities
                .stream()
                .map(t -> modelMapper.map(t, Turn.class))
                .collect(Collectors.toList());
    }

}
