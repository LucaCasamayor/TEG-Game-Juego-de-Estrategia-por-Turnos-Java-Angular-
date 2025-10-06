package ar.edu.utn.frc.tup.piii.services.implementations;

import ar.edu.utn.frc.tup.piii.entities.MovementEntity;
import ar.edu.utn.frc.tup.piii.entities.TerritoryStateEntity;
import ar.edu.utn.frc.tup.piii.entities.TurnEntity;
import ar.edu.utn.frc.tup.piii.enums.TurnPhase;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryStateMapper;
import ar.edu.utn.frc.tup.piii.mappers.impl.TerritoryMapperImpl;
import ar.edu.utn.frc.tup.piii.mappers.impl.TerritoryStateMapperImpl;
import ar.edu.utn.frc.tup.piii.mappers.impl.TurnMapperImpl;
import ar.edu.utn.frc.tup.piii.models.*;
import ar.edu.utn.frc.tup.piii.persistence.PlayerPersistence;
import ar.edu.utn.frc.tup.piii.persistence.TurnPersistence;
import ar.edu.utn.frc.tup.piii.repository.jpa.TurnJpaRepository;
import ar.edu.utn.frc.tup.piii.services.DiceService;
import ar.edu.utn.frc.tup.piii.services.GamePersistenceService;
import ar.edu.utn.frc.tup.piii.services.TerritoryService;
import ar.edu.utn.frc.tup.piii.services.TerritoryStateService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TurnServiceCommon {

    private final TurnPersistence persistence;
    private final TerritoryService territoryService;
    private final DiceService diceService;
    private final GamePersistenceService gamePersistenceService;
    private final TerritoryStateService territoryStateService;
    private final TerritoryStateMapper territoryStateMapper;
    private final PlayerPersistence playerPersistence;


    public Turn createTurn(Turn turn) {
        turn = persistence.saveTurn(turn);
        Game g = gamePersistenceService.getGameById(turn.getGameId());
        int deployCount = calculateReinforcements(turn);

        Movement deployMovement = new Movement();
        deployMovement.setArmyCount(deployCount);
        if (persistence.findByPlayer(turn.getPlayer().getPlayerId()).size() == 3) {
            turn.setTurnPhase(TurnPhase.ATTACK);
        } else {
            turn.setTurnPhase(TurnPhase.DEPLOYMENT);
        }
        turn.setMovements(List.of(deployMovement));
        turn = persistence.updateTurn(turn.getTurnId(), turn);

        g = gamePersistenceService.getGameById(g.getGameId());

        return persistence.updateTurn(turn.getTurnId(), turn);
    }


    public Turn endTurn(Turn turn) {

        Game game = gamePersistenceService.getGameById(turn.getGameId());
        Turn nextTurn = new Turn();
        int nextPlayerTurnOrder;
        if (turn.getPlayer().getTurnOrder() == game.getPlayers().size()) {
            nextPlayerTurnOrder = 0;
        } else {
            nextPlayerTurnOrder = turn.getPlayer().getTurnOrder();
        }
        if (game.getPlayers().get(nextPlayerTurnOrder).isHasLost()) {
            if (nextPlayerTurnOrder + 1 == game.getPlayers().size()) {
                nextPlayerTurnOrder = 0;
            } else {
                nextPlayerTurnOrder += 1;
            }
        }
        nextTurn.setGameId(game.getGameId());
        nextTurn.setPlayer(game.getPlayers().get(nextPlayerTurnOrder));

        nextTurn = createTurn(nextTurn);

        return nextTurn;
    }

//    public Turn updateTurnPhase(Turn turn) {
//        switch (turn.getTurnPhase()) {
//            case DEPLOYMENT:
//                if (!getPossibleAttackers(turn).isEmpty()) {
//                    turn.setTurnPhase(TurnPhase.ATTACK);
//                } else if (!getPossibleFortifiers(turn).isEmpty()) {
//                    turn.setTurnPhase(TurnPhase.FORTIFY);
//                } else {
//                    turn.setTurnPhase(TurnPhase.DONE);
//                    turn = persistence.updateTurn(turn.getTurnId(), turn);
//                    return endTurn(turn);
//                }
//                break;
//            case ATTACK:
//                if (!getPossibleFortifiers(turn).isEmpty()) {
//                    turn.setTurnPhase(TurnPhase.FORTIFY);
//                } else {
//                    turn.setTurnPhase(TurnPhase.CARD);
//                }
//                turn = checkObjectiveStates(turn);
//                break;
//            case FORTIFY:
//                turn.setTurnPhase(TurnPhase.CARD);
//            case CARD:
//                turn.setTurnPhase(TurnPhase.DONE);
//                turn = persistence.updateTurn(turn.getTurnId(), turn);
//                return endTurn(turn);
//        }
//        return persistence.updateTurn(turn.getTurnId(), turn);
//
//
//    }

    public Turn updateTurnPhase(Turn turn) {
        switch (turn.getTurnPhase()) {
            case DEPLOYMENT -> turn.setTurnPhase(TurnPhase.ATTACK);
            case ATTACK -> turn.setTurnPhase(TurnPhase.FORTIFY);
            case FORTIFY -> turn.setTurnPhase(TurnPhase.CARD);
            case CARD -> {
                turn.setTurnPhase(TurnPhase.DONE);
                turn = persistence.updateTurn(turn.getTurnId(), turn);
                return endTurn(turn);
            }
        }
        return persistence.updateTurn(turn.getTurnId(), turn);
    }



    public Integer calculateReinforcements(Turn turn) {

        //TODO Si llegamos con el tiempo cambiar logica de bonus como funciones aparte
        List<Turn> playerTurns = persistence.findByPlayer(turn.getPlayer().getPlayerId());
        int playerTurnNumber = playerTurns.size();

        if (playerTurnNumber == 1) {
            return 5;
        } else if (playerTurnNumber == 2) {
            return 3;
        } else if (playerTurnNumber == 3) {
            return 0;
        }
        List<TerritoryState> territories = territoryStateService.findByPlayer(turn.getPlayer().getPlayerId())
                .stream().map(territoryStateMapper::toModel).toList();
        int territoryCount = territories.size();
        int base = (Math.max((territoryCount / 2), 3));

//        Map<Region, Integer> contadorFrecuencia = new HashMap<>();
//        for (TerritoryState ts : turn.getPlayer().getTerritories()) {
//            contadorFrecuencia.put(ts.getTerritory().getRegion(), contadorFrecuencia.getOrDefault(ts.getTerritory().getRegion(), 0) + 1);
//        }
//        int bonus = 0;
//        for (Region r : contadorFrecuencia.keySet()) {
//            if (r.getNumberOfTerritories() == contadorFrecuencia.get(r)) {
//                bonus += r.getNumberOfTerritories() / 2;
//            }
//        }
        return base;
    }

    public List<TerritoryState> getOwnBorderingTerritories(Turn turn, TerritoryState ts) {
        List<TerritoryState> ownTerritories = new ArrayList<>();

        Long territoryId = ts.getTerritory().getTerritoryId();

        List<Territory> borderingTerritories = territoryService.getBorderingCountries(territoryId);

        for (TerritoryState territory : turn.getPlayer().getTerritories()) {

            if (borderingTerritories.contains(territory.getTerritory()) && !ownTerritories.contains(territory)) {
                ownTerritories.add(territory);
            }
        }
        return ownTerritories;
    }

    public List<TerritoryState> getPossibleAttackers(Turn turn) {
        Player currentPlayer = turn.getPlayer();
        List<TerritoryState> ownTerritories = new ArrayList<>(List.copyOf(currentPlayer.getTerritories()));
        ownTerritories.removeIf(ts -> ts.getArmyCount() < 2);
        ownTerritories.removeIf(ts -> getEnemyTerritories(turn, ts).isEmpty());
        return ownTerritories;
    }

    public List<TerritoryState> getPossibleFortifiers(Turn turn) {
        Player currentPlayer = turn.getPlayer();
        List<TerritoryState> ownTerritories = new ArrayList<>(List.copyOf(currentPlayer.getTerritories()));
        ownTerritories.removeIf(ts -> ts.getArmyCount() < 2);
        ownTerritories.removeIf(ts -> getOwnBorderingTerritories(turn, ts).isEmpty());
        return ownTerritories;
    }

    public List<TerritoryState> getEnemyTerritories(Turn turn, TerritoryState ts) {
        List<TerritoryState> borderingEnemyTerritories = new ArrayList<>();

        Territory ownTerritory = ts.getTerritory();
        Long territoryId = ownTerritory.getTerritoryId();

        List<Territory> borderingTerritories = territoryService.getBorderingCountries(territoryId);

        List<TerritoryState> allEnemyTerritories = new ArrayList<>();
        for (Player enemyPlayer : gamePersistenceService.getGameById(turn.getGameId()).getPlayers()) {
            if (!enemyPlayer.getPlayerId().equals(ts.getPlayerId())) {
                allEnemyTerritories.addAll(enemyPlayer.getTerritories());
            }
        }

        for (TerritoryState enemyTS : allEnemyTerritories) {
            Territory enemyTerritory = enemyTS.getTerritory();
            if (borderingTerritories.contains(enemyTerritory) && !borderingEnemyTerritories.contains(enemyTS)) {
                borderingEnemyTerritories.add(enemyTS);
            }
        }

        return borderingEnemyTerritories;
    }

    public boolean attackOutcome(Turn turn, TerritoryState attacker, TerritoryState target) {
        List<Movement> movements = turn.getMovements();
        Movement currentMovement = movements.get(turn.getMovements().size() - 1);

        List<Dice> result = diceService.generateDiceResults(currentMovement,
                currentMovement.getArmyCount(), target.getArmyCount());

        movements.get(turn.getMovements().size() - 1).setDice(result);
        turn.setMovements(movements);

        for (Dice d : result) {
            if (d.getAttackerDice() == 0 || d.getDefenderDice() == 0) break;

            if (d.getAttackerDice() > d.getDefenderDice()) {
                target.setArmyCount(target.getArmyCount() - 1);
            } else {
                attacker.setArmyCount(attacker.getArmyCount() - 1);
            }
        }

        if (target.getArmyCount() == 0) {
            target.setPlayerId(attacker.getPlayerId());
            persistence.updateTurn(turn.getTurnId(), turn);
            return true;
        }

        Turn t = persistence.updateTurn(turn.getTurnId(), turn);
        return false;
    }

    public List<Turn> getTurnsByGame(String gameId) {
        UUID gameUUID = UUID.fromString(gameId);
        return persistence.findByGame(gameUUID);
    }


    public boolean didPlayerConquerThisTurn(Long playerId, Long turnId) {
        if (playerId == null || turnId == null) {
            throw new IllegalArgumentException("playerId y turnId no pueden ser nulos.");
        }

        Turn turn = persistence.findById(turnId);
        if (turn == null) {
            throw new EntityNotFoundException("Turno no encontrado con ID: " + turnId);
        }

        // Validar que el turno pertenezca al jugador
        if (!turn.getPlayer().getPlayerId().equals(playerId)) {
            throw new IllegalStateException("Este turno no pertenece al jugador especificado.");
        }

        List<Movement> movements = turn.getMovements();
        if (movements == null || movements.isEmpty()) {
            return false;
        }

        for (Movement movement : movements) {
            TerritoryState endTerritory = territoryStateService.findByIdModel(movement.getEndTerritoryId());

            if (endTerritory == null || endTerritory == null) continue;

            if (!movement.getMovementType().equals("ATTACK")) continue;

            // Si el territorio pasó a ser del jugador, consideramos que fue conquistado
            if (endTerritory.equals(playerId)) {
                return true;
            }
        }

        return false;
    }

    public Turn checkObjectiveStates(Turn turn) {
        Game game = gamePersistenceService.getGameById(turn.getGameId());
        Player currentPlayer = null;
        boolean isWinner = false;
        for (Player p : game.getPlayers()) {
            if (p.getPlayerId().equals(turn.getPlayer().getPlayerId())) {
                currentPlayer = p;
            }
        }
        if (currentPlayer != null) {

            Objective currentObjective = currentPlayer.getObjective();

            switch (currentObjective.getObjectiveType().getName()) {
                case "Conquest":
                    isWinner = checkConquestObjective(currentObjective, currentPlayer);

                    break;
                case "Destruction":
                    isWinner = checkDestructionObjective(currentObjective, game.getPlayers());
                    break;
            }

            if (!isWinner && currentPlayer.getTerritories().size() >= 30) {
                isWinner = true;
            }
            if (isWinner) {
                currentPlayer.setWinner(isWinner);
                playerPersistence.updatePlayer(currentPlayer.getPlayerId(), currentPlayer);
            }
            return persistence.updateTurn(turn.getTurnId(), turn);
        } else {
            throw new RuntimeException("No se pudo revisar los objetivos porque el jugador era null");
        }
    }

    private boolean checkDestructionObjective(Objective currentObjective, List<Player> players) {
        for (Player p : players) {
            if (p.getPlayerColor() == currentObjective.getColor()) {
                return !p.isHasLost();
            }
        }
        return false;
    }

    private boolean checkConquestObjective(Objective currentObjective, Player currentPlayer) {
        int[] regionCounts = new int[6];

        for (TerritoryState ts : currentPlayer.getTerritories()) {
            int regionIndex = (int) (ts.getTerritory().getRegion().getId() % 6);
            regionCounts[regionIndex]++;
        }

        int[] requiredCounts = new int[]{
                currentObjective.getSixthRegionTerritoriesNeeded(),
                currentObjective.getFirstRegionTerritoriesNeeded(),
                currentObjective.getSecondRegionTerritoriesNeeded(),
                currentObjective.getThirdRegionTerritoriesNeeded(),
                currentObjective.getFourthRegionTerritoriesNeeded(),
                currentObjective.getFifthRegionTerritoriesNeeded()
        };

        for (int i = 0; i < 6; i++) {
            if (regionCounts[i] < requiredCounts[i]) {
                return false;
            }
        }

        return true;
    }

}
