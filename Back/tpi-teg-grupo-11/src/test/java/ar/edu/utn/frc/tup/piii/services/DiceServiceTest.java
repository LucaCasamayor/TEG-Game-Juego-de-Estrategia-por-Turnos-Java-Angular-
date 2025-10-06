package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.models.Dice;
import ar.edu.utn.frc.tup.piii.models.Movement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DiceServiceTest {

    @InjectMocks
    private DiceService diceService;

    private Movement movement;

    @BeforeEach
    void setUp() {
        movement = new Movement();
    }

    @Test
    void testGenerateDiceResults_BothArmiesHaveMaxDice() {
        int armyCountAtt = 5; // Should use 3 dice (max)
        int armyCountDef = 4; // Should use 3 dice (max)

        List<Dice> result = diceService.generateDiceResults(movement, armyCountAtt, armyCountDef);

        assertEquals(3, result.size());

        for (Dice dice : result) {
            assertTrue(dice.getAttackerDice() >= 1 && dice.getAttackerDice() <= 6,
                    "Attacker dice should be between 1-6");
            assertTrue(dice.getDefenderDice() >= 1 && dice.getDefenderDice() <= 6,
                    "Defender dice should be between 1-6");
        }

        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i).getAttackerDice() >= result.get(i + 1).getAttackerDice(),
                    "Attacker dice should be sorted in descending order");
            assertTrue(result.get(i).getDefenderDice() >= result.get(i + 1).getDefenderDice(),
                    "Defender dice should be sorted in descending order");
        }
    }

    @Test
    void testGenerateDiceResults_AttackerHasMoreDiceThanDefender() {
        // Arrange
        int armyCountAtt = 3; // Should use 3 dice
        int armyCountDef = 1; // Should use 1 dice

        List<Dice> result = diceService.generateDiceResults(movement, armyCountAtt, armyCountDef);

        assertEquals(3, result.size());

        assertTrue(result.get(0).getAttackerDice() >= 1 && result.get(0).getAttackerDice() <= 6);
        assertTrue(result.get(0).getDefenderDice() >= 1 && result.get(0).getDefenderDice() <= 6);

        assertTrue(result.get(1).getAttackerDice() >= 1 && result.get(1).getAttackerDice() <= 6);
        assertEquals(0, result.get(1).getDefenderDice());

        assertTrue(result.get(2).getAttackerDice() >= 1 && result.get(2).getAttackerDice() <= 6);
        assertEquals(0, result.get(2).getDefenderDice());

        assertTrue(result.get(0).getAttackerDice() >= result.get(1).getAttackerDice());
        assertTrue(result.get(1).getAttackerDice() >= result.get(2).getAttackerDice());
    }

    @Test
    void testGenerateDiceResults_DefenderHasMoreDiceThanAttacker() {
        int armyCountAtt = 1; // Should use 1 dice
        int armyCountDef = 3; // Should use 3 dice

        List<Dice> result = diceService.generateDiceResults(movement, armyCountAtt, armyCountDef);

        assertEquals(3, result.size());

        assertTrue(result.get(0).getAttackerDice() >= 1 && result.get(0).getAttackerDice() <= 6);
        assertTrue(result.get(0).getDefenderDice() >= 1 && result.get(0).getDefenderDice() <= 6);

        assertEquals(0, result.get(1).getAttackerDice());
        assertTrue(result.get(1).getDefenderDice() >= 1 && result.get(1).getDefenderDice() <= 6);

        assertEquals(0, result.get(2).getAttackerDice());
        assertTrue(result.get(2).getDefenderDice() >= 1 && result.get(2).getDefenderDice() <= 6);

        assertTrue(result.get(0).getDefenderDice() >= result.get(1).getDefenderDice());
        assertTrue(result.get(1).getDefenderDice() >= result.get(2).getDefenderDice());
    }

    @Test
    void testGenerateDiceResults_BothArmiesHaveSameDiceCount() {
        int armyCountAtt = 2; // Should use 2 dice
        int armyCountDef = 2; // Should use 2 dice

        List<Dice> result = diceService.generateDiceResults(movement, armyCountAtt, armyCountDef);

        assertEquals(2, result.size());

        for (Dice dice : result) {
            assertTrue(dice.getAttackerDice() >= 1 && dice.getAttackerDice() <= 6);
            assertTrue(dice.getDefenderDice() >= 1 && dice.getDefenderDice() <= 6);
        }

        assertTrue(result.get(0).getAttackerDice() >= result.get(1).getAttackerDice());
        assertTrue(result.get(0).getDefenderDice() >= result.get(1).getDefenderDice());
    }

    @Test
    void testGenerateDiceResults_MinimumArmyCount() {
        int armyCountAtt = 1; // Should use 1 dice
        int armyCountDef = 1; // Should use 1 dice

        List<Dice> result = diceService.generateDiceResults(movement, armyCountAtt, armyCountDef);


        assertEquals(1, result.size());
        assertTrue(result.get(0).getAttackerDice() >= 1 && result.get(0).getAttackerDice() <= 6);
        assertTrue(result.get(0).getDefenderDice() >= 1 && result.get(0).getDefenderDice() <= 6);
    }

    @Test
    void testGenerateDiceResults_MaximumArmyCountExceeded() {
        int armyCountAtt = 10; // Should use 3 dice (max)
        int armyCountDef = 8;  // Should use 3 dice (max)

        List<Dice> result = diceService.generateDiceResults(movement, armyCountAtt, armyCountDef);

        assertEquals(3, result.size(), "Should only use maximum 3 dice even with large army counts");

        for (Dice dice : result) {
            assertTrue(dice.getAttackerDice() >= 1 && dice.getAttackerDice() <= 6);
            assertTrue(dice.getDefenderDice() >= 1 && dice.getDefenderDice() <= 6);
        }

        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i).getAttackerDice() >= result.get(i + 1).getAttackerDice());
            assertTrue(result.get(i).getDefenderDice() >= result.get(i + 1).getDefenderDice());
        }
    }

    @Test
    void testGenerateDiceResults_DiceAreSortedInDescendingOrder() {

        int armyCountAtt = 3;
        int armyCountDef = 3;

        List<Dice> result = diceService.generateDiceResults(movement, armyCountAtt, armyCountDef);

        assertEquals(3, result.size());

        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i).getAttackerDice() >= result.get(i + 1).getAttackerDice(),
                    "Attacker dice should be sorted in descending order");
            assertTrue(result.get(i).getDefenderDice() >= result.get(i + 1).getDefenderDice(),
                    "Defender dice should be sorted in descending order");
        }

        for (Dice dice : result) {
            assertTrue(dice.getAttackerDice() >= 1 && dice.getAttackerDice() <= 6);
            assertTrue(dice.getDefenderDice() >= 1 && dice.getDefenderDice() <= 6);
        }
    }

    @Test
    void testGenerateDiceResults_ZeroDiceCount() {
        int armyCountAtt = 0; // Should use 0 dice
        int armyCountDef = 2; // Should use 2 dice

        List<Dice> result = diceService.generateDiceResults(movement, armyCountAtt, armyCountDef);

        assertEquals(2, result.size(), "Should create dice based on maximum count");

        for (Dice dice : result) {
            assertEquals(0, dice.getAttackerDice(), "Attacker dice should be 0 when army count is 0");
            assertTrue(dice.getDefenderDice() >= 1 && dice.getDefenderDice() <= 6,
                    "Defender dice should be valid when army count > 0");
        }

        assertTrue(result.get(0).getDefenderDice() >= result.get(1).getDefenderDice());
    }

    @Test
    void testGenerateDiceResults_DefenderZeroDiceCount() {
        int armyCountAtt = 2; // Should use 2 dice
        int armyCountDef = 0; // Should use 0 dice

        List<Dice> result = diceService.generateDiceResults(movement, armyCountAtt, armyCountDef);

        assertEquals(2, result.size(), "Should create dice based on maximum count");

        for (Dice dice : result) {
            assertTrue(dice.getAttackerDice() >= 1 && dice.getAttackerDice() <= 6,
                    "Attacker dice should be valid when army count > 0");
            assertEquals(0, dice.getDefenderDice(), "Defender dice should be 0 when army count is 0");
        }

        assertTrue(result.get(0).getAttackerDice() >= result.get(1).getAttackerDice());
    }

    @Test
    void testGenerateDiceResults_BothZeroDiceCount() {
        int armyCountAtt = 0; // Should use 0 dice
        int armyCountDef = 0; // Should use 0 dice

        List<Dice> result = diceService.generateDiceResults(movement, armyCountAtt, armyCountDef);

        assertEquals(0, result.size(), "Should return empty list when both armies have 0 count");
    }

    @Test
    void testGenerateDiceResults_DiceValuesWithinValidRange() {
        int armyCountAtt = 3;
        int armyCountDef = 3;

        for (int test = 0; test < 10; test++) {
            List<Dice> result = diceService.generateDiceResults(movement, armyCountAtt, armyCountDef);

            for (Dice dice : result) {
                assertTrue(dice.getAttackerDice() >= 1 && dice.getAttackerDice() <= 6,
                        "Attacker dice value should be between 1-6, got: " + dice.getAttackerDice());
                assertTrue(dice.getDefenderDice() >= 1 && dice.getDefenderDice() <= 6,
                        "Defender dice value should be between 1-6, got: " + dice.getDefenderDice());
            }
        }
    }
}