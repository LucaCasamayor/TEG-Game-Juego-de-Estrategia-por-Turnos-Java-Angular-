package ar.edu.utn.frc.tup.piii.services.implementations;

import ar.edu.utn.frc.tup.piii.enums.TurnPhase;
import ar.edu.utn.frc.tup.piii.models.Dice;
import ar.edu.utn.frc.tup.piii.models.Movement;
import ar.edu.utn.frc.tup.piii.models.TerritoryState;
import ar.edu.utn.frc.tup.piii.models.Turn;
import ar.edu.utn.frc.tup.piii.persistence.TurnPersistence;
import ar.edu.utn.frc.tup.piii.services.DiceService;
import ar.edu.utn.frc.tup.piii.services.TerritoryStateService;
import ar.edu.utn.frc.tup.piii.services.interfaces.TurnService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("turnHumanImpl")
@RequiredArgsConstructor
@Primary
public class TurnHumanImpl implements TurnService {

    private final TurnServiceCommon turnServiceCommon;
    private final TerritoryStateService territoryStateService;
    private final TurnPersistence turnPersistence;
    private final DiceService diceService;

    @Override
    public Turn playDeployStage(Turn turn) throws Exception {

        if (!turn.getTurnPhase().equals(TurnPhase.DEPLOYMENT)) {
            throw new Exception("El turno no está en fase de Despliegue");
        }
        if (turnPersistence.findByPlayer(turn.getPlayer().getPlayerId()).size() == 3) {
            return turnServiceCommon.updateTurnPhase(turn);
        }
        List<Movement> turnMovements = turn.getMovements();
        Movement firstMovement = turnMovements.get(0);
        Movement lastMovement = turnMovements.get(turnMovements.size() - 1);

        if (firstMovement.getArmyCount() - lastMovement.getArmyCount() < 0) {
            throw new Exception("No se pueden desplegar más tropas");
        } else {
            firstMovement.setArmyCount(firstMovement.getArmyCount() - lastMovement.getArmyCount());
        }

        TerritoryState end = territoryStateService.findByIdModel(lastMovement.getEndTerritoryId());

        if (!end.getPlayerId().equals(turn.getPlayer().getPlayerId())) {
            throw new Exception("El territorio no corresponde al usuario del turno");
        }

        lastMovement.setMovementType(turn.getTurnPhase());
        end.setArmyCount(end.getArmyCount() + lastMovement.getArmyCount());
        territoryStateService.updateModel(end.getTerritoryStateId(), end);
        turnPersistence.updateTurn(turn.getTurnId(), turn);

        if (firstMovement.getArmyCount() == 0) {
            //TODO Reactivar esto cuando terminemos logica de ronda para que se de la vuelta
            // por todos los jugadores 2 veces antes de la primera fase de ataque
            if(turnPersistence.findByPlayer(turn.getPlayer().getPlayerId()).size()== 1
            || turnPersistence.findByPlayer(turn.getPlayer().getPlayerId()).size()== 2){
                turn.setTurnPhase(TurnPhase.DONE);
                turnServiceCommon.endTurn(turn);
            }
        }

        return turnPersistence.updateTurn(turn.getTurnId(), turn);
    }

    @Override
    public Turn playAttackStage(Turn turn) throws Exception {
        Movement lastMovement = turn.getMovements().get(turn.getMovements().size() - 1);

        if (!turn.getTurnPhase().equals(TurnPhase.ATTACK)) {
            throw new Exception("El turno no está en fase de Ataque");
        }

        TerritoryState start = territoryStateService.findByIdModel(lastMovement.getStartTerritoryId());
        List<TerritoryState> territoriesByPlayer = territoryStateService.findByPlayerModel(turn.getPlayer().getPlayerId());
        if (!territoriesByPlayer.contains(start)) {
            throw new Exception("El territorio no corresponde al usuario del turno");
        }

        if (lastMovement.getStartTerritoryId() != null && lastMovement.getEndTerritoryId() != null && lastMovement.getArmyCount() > 0) { // segunda subfase
            List<TerritoryState> attackOptions = turnServiceCommon.getEnemyTerritories(turn, start);
            TerritoryState end = territoryStateService.findByIdModel(lastMovement.getEndTerritoryId());
            if (attackOptions.contains(end)) {

                lastMovement.setMovementType(turn.getTurnPhase());
                List<Dice> results = diceService.generateDiceResults(lastMovement, start.getArmyCount() - 1, end.getArmyCount());
                lastMovement.setDice(results);
                lastMovement.setArmyCount(Math.min(3, lastMovement.getArmyCount()));

                for (Dice d : results) {
                    if (d.getAttackerDice() != 0 &&
                            d.getDefenderDice() != 0){
                        if (d.getAttackerDice() > d.getDefenderDice()) {
                            end.setArmyCount(end.getArmyCount() - 1);
                        } else {
                            start.setArmyCount(start.getArmyCount() - 1);
                        }
                    }
                }

                if (end.getArmyCount() == 0) {
                    end.setPlayerId(start.getPlayerId());
                    //TODO revisar como hacer que el usuario elija cuantos ejércitos mover
                    end.setArmyCount(start.getArmyCount() - 1);
                    start.setArmyCount(1);
                }
                territoryStateService.updateModel(start.getTerritoryStateId(), start);
                territoryStateService.updateModel(end.getTerritoryStateId(), end);
            } else {
                throw new Exception("El territorio no es un posible ataque");
            }
        }
        Turn currentTurn = turnPersistence.updateTurn(turn.getTurnId(), turn);

        List<TerritoryState> possibleAttacks = turnServiceCommon.getPossibleAttackers(currentTurn);
        if (possibleAttacks.isEmpty()) {
            return turnServiceCommon.updateTurnPhase(turn);
        }

        return turnPersistence.updateTurn(turn.getTurnId(), turn);
    }

    @Override
    public Turn playFortifyStage(Turn turn) throws Exception {
        Movement lastMovement = turn.getMovements().get(turn.getMovements().size() - 1);

        if (!turn.getTurnPhase().equals(TurnPhase.FORTIFY)) {
            throw new Exception("El turno no está en fase de Fortificación");
        }

        List<TerritoryState> possibleFortifiers = turnServiceCommon.getPossibleFortifiers(turn);
        if (possibleFortifiers.isEmpty()) {
            return turnServiceCommon.updateTurnPhase(turn);
        }

        TerritoryState start = territoryStateService.findByIdModel(lastMovement.getStartTerritoryId());
        List<TerritoryState> territoriesByPlayer = territoryStateService.findByPlayerModel(turn.getPlayer().getPlayerId());
        if (!territoriesByPlayer.contains(start)) {
            throw new Exception("El territorio no corresponde al usuario del turno");
        }

        //TODO agregar logica para evitar fortificar varios paises
        // seguidos (agregar reinforcements al front y descomentar del back)

        if (lastMovement.getStartTerritoryId() != null && lastMovement.getEndTerritoryId() != null && lastMovement.getArmyCount() > 0) { // segunda subfase
            List<TerritoryState> fortifyOptions = turnServiceCommon.getOwnBorderingTerritories(turn, start);
            TerritoryState end = territoryStateService.findByIdModel(lastMovement.getEndTerritoryId());
            lastMovement.setMovementType(turn.getTurnPhase());
            if (fortifyOptions.contains(end)) {
                end.setArmyCount(end.getArmyCount() + lastMovement.getArmyCount());
                start.setArmyCount(start.getArmyCount() - lastMovement.getArmyCount());
            }
            territoryStateService.updateModel(start.getTerritoryStateId(), start);
            territoryStateService.updateModel(end.getTerritoryStateId(), end);
        } else {
            throw new Exception("El territorio no puede ser fortificado");
        }

        return turnPersistence.updateTurn(turn.getTurnId(), turn);
    }

    @Override
    public Turn playCardStage(Turn turn) {
        return null;
    }

}
