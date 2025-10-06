package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.models.Dice;
import ar.edu.utn.frc.tup.piii.models.Movement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
@RequiredArgsConstructor
@Service
public class DiceService {

    public List<Dice> generateDiceResults(Movement m, int armyCountAtt, int armyCountDef) {
        Random random = new Random();

        List<Integer> attackerDiceResults = random.ints(Math.min(armyCountAtt, 3), 1, 7)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .toList();

        List<Integer> defenderDiceResults = random.ints(Math.min(armyCountDef, 3), 1, 7)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .toList();

        List<Dice> result = new ArrayList<>();
        int maxSize = Math.max(attackerDiceResults.size(), defenderDiceResults.size());

        for (int i = 0; i < maxSize; i++) {
            Dice dice = new Dice();
            dice.setAttackerDice(i < attackerDiceResults.size() ? attackerDiceResults.get(i) : 0);
            dice.setDefenderDice(i < defenderDiceResults.size() ? defenderDiceResults.get(i) : 0);
            result.add(dice);
        }
        return result;
    }

}

