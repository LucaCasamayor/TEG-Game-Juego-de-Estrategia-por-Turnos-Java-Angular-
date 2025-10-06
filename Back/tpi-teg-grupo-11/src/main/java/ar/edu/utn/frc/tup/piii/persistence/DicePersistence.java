package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.DiceEntity;
import ar.edu.utn.frc.tup.piii.models.Dice;
import ar.edu.utn.frc.tup.piii.repository.jpa.DiceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DicePersistence {
    private final DiceJpaRepository diceJpaRepository;
    private final MovementPersistence movementPersistence;
    private final ModelMapper modelMapper;

    public Dice createDice(Dice dice) {
        DiceEntity diceEntity = new DiceEntity();
        DiceEntity savedEntity = diceJpaRepository.save(diceEntity);
        return modelMapper.map(savedEntity, Dice.class);
    }

//    public void deleteDice(Long diceId) {
//        diceJpaRepository.deleteById(diceId);
//    }
//
//    public List<Dice> findDiceByMovement(Long movementId) {
//        MovementEntity movement = modelMapper.map(movementPersistence.getMovementById(movementId), MovementEntity.class);
//        return diceJpaRepository.findAllByMovement(movement)
//                .stream()
//                .map(dice -> modelMapper.map(dice, Dice.class))
//                .collect(Collectors.toList());
//    }

}
