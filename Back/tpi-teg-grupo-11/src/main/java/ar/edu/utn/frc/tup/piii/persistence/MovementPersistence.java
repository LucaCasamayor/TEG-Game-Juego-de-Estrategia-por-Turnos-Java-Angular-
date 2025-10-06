package ar.edu.utn.frc.tup.piii.persistence;


import ar.edu.utn.frc.tup.piii.entities.MovementEntity;
import ar.edu.utn.frc.tup.piii.models.Movement;
import ar.edu.utn.frc.tup.piii.models.Turn;
import ar.edu.utn.frc.tup.piii.repository.jpa.MovementJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovementPersistence {

    private final MovementJpaRepository repository;
    private final TurnPersistence turnPersistence;
    private final ModelMapper modelMapper;

    public Movement getMovementById(Long id){
        MovementEntity me = repository.getReferenceById(id);
        return modelMapper.map(me, Movement.class);
    }

    public Movement saveMovement(Movement movement){
        MovementEntity me = modelMapper.map(movement, MovementEntity.class);
        return modelMapper.map(repository.save(me), Movement.class);
    }

    public List<Movement> findAllByGameId(String gameId) {
        UUID uuid = UUID.fromString(gameId);
        List<Turn> turns = turnPersistence.findByGame(uuid);

        List<Movement> allMovements = new ArrayList<>();
        for (Turn t : turns) {
            allMovements.addAll(t.getMovements());
        }

        return allMovements;
    }
}
