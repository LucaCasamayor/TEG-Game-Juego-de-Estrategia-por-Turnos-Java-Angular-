package ar.edu.utn.frc.tup.piii.services.implementations;

import ar.edu.utn.frc.tup.piii.dtos.TerritoryStateDto;
import ar.edu.utn.frc.tup.piii.enums.TurnPhase;
import ar.edu.utn.frc.tup.piii.models.Movement;
import ar.edu.utn.frc.tup.piii.models.TerritoryState;
import ar.edu.utn.frc.tup.piii.models.Turn;
import ar.edu.utn.frc.tup.piii.persistence.TurnPersistence;
import ar.edu.utn.frc.tup.piii.services.TerritoryStateService;
import ar.edu.utn.frc.tup.piii.services.interfaces.TurnService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service("turnBotEasyImpl")
@RequiredArgsConstructor
public class TurnBotEasyImpl implements TurnService {

    private final TerritoryStateService territoryStateService;
    private final TurnServiceCommon turnServiceCommon;
    private final TurnPersistence turnPersistence;
    private final ModelMapper modelMapper;


    @Override
    public Turn playDeployStage(Turn turn) throws Exception {
        if (!turn.getTurnPhase().equals(TurnPhase.DEPLOYMENT)) {
            throw new Exception("El turno no está en fase de Despliegue");
        }

        List<Movement> turnMovements = turn.getMovements();
        Movement firstMovement = turnMovements.get(0);
        if (firstMovement.getArmyCount() == 0) {
            //TODO Reactivar esto cuando terminemos logica de ronda para que se de la vuelta
            // por todos los jugadores 2 veces antes de la primera fase de ataque
            if(turnPersistence.findByPlayer(turn.getPlayer().getPlayerId()).size()== 1
                    || turnPersistence.findByPlayer(turn.getPlayer().getPlayerId()).size()== 2){
                turn.setTurnPhase(TurnPhase.DONE);
                turnServiceCommon.endTurn(turn);
            }
            return turnServiceCommon.updateTurnPhase(turn);

        }
        List<TerritoryState> territories = turn.getPlayer().getTerritories();
        Random random = new Random();
        TerritoryState randomEndTerritory = territories.get(random.nextInt(territories.size()));
        randomEndTerritory.setPlayerId(turn.getPlayer().getPlayerId());
        Movement lastMovement = new Movement();
        lastMovement.setArmyCount(random.nextInt(firstMovement.getArmyCount()) + 1);
        lastMovement.setMovementType(turn.getTurnPhase());
        lastMovement.setEndTerritoryId(randomEndTerritory.getTerritoryStateId());
        turnMovements.add(lastMovement);

        if (firstMovement.getArmyCount() - lastMovement.getArmyCount() < 0) {
            throw new Exception("No se pueden desplegar más tropas");
        } else {
            firstMovement.setArmyCount(firstMovement.getArmyCount() - lastMovement.getArmyCount());
        }

        if (!turn.getPlayer().getTerritories().contains(randomEndTerritory)) {
            throw new Exception("El territorio no corresponde al usuario del turno");
        }else{
            randomEndTerritory.setPlayerId(turn.getPlayer().getPlayerId());
        }

        randomEndTerritory.setArmyCount(randomEndTerritory.getArmyCount() + lastMovement.getArmyCount());
        randomEndTerritory.setPlayerId(turn.getPlayer().getPlayerId());
        territoryStateService.update(randomEndTerritory.getTerritoryStateId(), modelMapper.map(randomEndTerritory, TerritoryStateDto.class));

        return turnPersistence.updateTurn(turn.getTurnId(), turn);
    }

    @Override
    public Turn playAttackStage(Turn turn) throws Exception {
        if (!turn.getTurnPhase().equals(TurnPhase.ATTACK)) {
            throw new Exception("El turno no está en fase de Ataque");
        }

        List<TerritoryState> botTerritories = turnServiceCommon.getPossibleAttackers(turn);
        if (botTerritories.isEmpty()) {
            return turnServiceCommon.updateTurnPhase(turn);
        }
        TerritoryState attacker = botTerritories.get(0);

        List<TerritoryState> botAttackOptions = turnServiceCommon.getEnemyTerritories(turn, attacker);
        Random random = new Random();
        TerritoryState target = botAttackOptions.get(random.nextInt(botAttackOptions.size()));

        Movement m = new Movement();
        m.setMovementType(turn.getTurnPhase());
        m.setStartTerritoryId(attacker.getTerritoryStateId());
        m.setEndTerritoryId(target.getTerritoryStateId());

        int attackingArmies = attacker.getArmyCount() - 1;
        m.setArmyCount(attackingArmies);
        List<Movement> movements = turn.getMovements();
        movements.add(m);
        m.setMovementType(turn.getTurnPhase());
        turn.setMovements(movements);

        if (turnServiceCommon.attackOutcome(turn, attacker, target)) {
            int movedTroops = attacker.getArmyCount() / 2;
            attacker.setArmyCount(attacker.getArmyCount() - movedTroops);
            target.setArmyCount(movedTroops);
            m.setArmyCount(attackingArmies - attacker.getArmyCount());
        }
        territoryStateService.updateModel(attacker.getTerritoryStateId(), attacker);
        territoryStateService.updateModel(target.getTerritoryStateId(), target);

        return turnPersistence.updateTurn(turn.getTurnId(), turn);
    }

    @Override
    public Turn playFortifyStage(Turn turn) throws Exception {
        if (!turn.getTurnPhase().equals(TurnPhase.FORTIFY)) {
            throw new Exception("El turno no está en fase de Fortificación");
        }

        List<TerritoryState> botTerritories = turnServiceCommon.getPossibleFortifiers(turn);
        Random random = new Random();
        if (botTerritories.isEmpty() || turn.getMovements().size() >= random.nextInt(6)) {
            return turnServiceCommon.updateTurnPhase(turn);
        }

        TerritoryState fortifier = botTerritories.get(random.nextInt(botTerritories.size()));
        List<TerritoryState> botFortifyOptions = turnServiceCommon.getOwnBorderingTerritories(turn, fortifier);
        TerritoryState target = botFortifyOptions.get(random.nextInt(botFortifyOptions.size()));

        Movement m = new Movement();
        m.setStartTerritoryId(fortifier.getTerritoryStateId());
        m.setEndTerritoryId(target.getTerritoryStateId());
        m.setMovementType(turn.getTurnPhase());
        int fortifyingArmies = random.nextInt(fortifier.getArmyCount() - 1);
        m.setArmyCount(fortifyingArmies);
        List<Movement> movements = turn.getMovements();
        movements.add(m);
        turn.setMovements(movements);
        fortifier.setArmyCount(fortifier.getArmyCount() - fortifyingArmies);
        target.setArmyCount(fortifyingArmies);
        territoryStateService.updateModel(fortifier.getTerritoryStateId(), fortifier);
        territoryStateService.updateModel(target.getTerritoryStateId(), target);

        //TODO agregar logica para evitar fortificar varios paises
        // seguidos (agregar reinforcements al front y descomentar del back)

        return turnPersistence.updateTurn(turn.getTurnId(), turn);
    }

    @Override
    public Turn playCardStage(Turn turn) {
//        TODO
//        Lograr que el Bot de baja dificultad
//        juegue su fase de CARTAS según la estrategia
//        propuesta en el enunciado del TPI
//        Reglas:
//          - Si tiene la posibilidad de reclamar alguna carta, que lo haga
//          - Tradea cartas al azar
        return turnServiceCommon.updateTurnPhase(turn);
    }
}
