package ar.edu.utn.frc.tup.piii.services.implementations;


import ar.edu.utn.frc.tup.piii.enums.TurnPhase;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryStateMapper;
import ar.edu.utn.frc.tup.piii.models.*;
import ar.edu.utn.frc.tup.piii.persistence.PlayerPersistence;
import ar.edu.utn.frc.tup.piii.persistence.TurnPersistence;
import ar.edu.utn.frc.tup.piii.repository.jpa.TurnJpaRepository;
import ar.edu.utn.frc.tup.piii.services.DiceService;
import ar.edu.utn.frc.tup.piii.services.GamePersistenceService;
import ar.edu.utn.frc.tup.piii.services.TerritoryService;
import ar.edu.utn.frc.tup.piii.services.TerritoryStateService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TurnServiceCommonTest {

    @Mock
    private TurnPersistence persistence;
    @Mock
    private TerritoryService territoryService;
    @Mock
    private TerritoryStateService territoryStateService;
    @Mock
    private GamePersistenceService gamePersistenceService;
    @Mock
    private PlayerPersistence playerPersistence;
    @Mock
    private DiceService diceService;
    @InjectMocks
    private TurnServiceCommon service;

    private Player createPlayer(Long id, int turnOrder, boolean hasLost) {
        Player player = new Player();
        player.setPlayerId(id);
        player.setTurnOrder(turnOrder);
        player.setHasLost(hasLost);
        player.setTerritories(new ArrayList<>());
        return player;
    }

    private TerritoryState createTerritoryState(Long territoryId, Long regionId, Long playerId, int armyCount) {
        Territory territory = new Territory();
        territory.setTerritoryId(territoryId);
        Region region = new Region();
        region.setId(regionId);
        territory.setRegion(region);

        TerritoryState ts = new TerritoryState();
        ts.setTerritory(territory);
        ts.setArmyCount(armyCount);
        ts.setPlayerId(playerId);
        return ts;
    }

    private Game mockGame(UUID gameId, List<Player> players) {
        Game game = new Game();
        game.setGameId(gameId);
        game.setPlayers(players);
        return game;
    }

    @Test
    void createTurn_shouldCreateDeploymentOrAttackTurn() {
        Turn turn = new Turn();
        Player player = createPlayer(1L, 1, false);
        turn.setPlayer(player);
        turn.setGameId(UUID.randomUUID());

        Game game = mockGame(turn.getGameId(), List.of(player));

        when(persistence.saveTurn(any())).thenReturn(turn);
        when(persistence.updateTurn(any(), any())).thenReturn(turn);
        when(gamePersistenceService.getGameById(any())).thenReturn(game);
        when(persistence.findByPlayer(player.getPlayerId())).thenReturn(List.of(turn, turn));

        Turn result = service.createTurn(turn);

        assertNotNull(result);
        assertEquals(1, result.getMovements().size());
    }

    @Test
    void endTurn_shouldAdvanceToNextPlayer() {
        Player p1 = createPlayer(1L, 1, false);
        Player p2 = createPlayer(2L, 2, true); // hasLost = true
        Player p3 = createPlayer(3L, 3, false);
        Game game = mockGame(UUID.randomUUID(), List.of(p1, p2, p3));

        Turn currentTurn = new Turn();
        currentTurn.setPlayer(p1);
        currentTurn.setGameId(game.getGameId());

        Turn newTurn = new Turn();
        newTurn.setPlayer(p3);
        newTurn.setGameId(game.getGameId());

        when(gamePersistenceService.getGameById(game.getGameId())).thenReturn(game);
        when(persistence.findByPlayer(any())).thenReturn(List.of(currentTurn, currentTurn, currentTurn));
        when(persistence.saveTurn(any())).thenReturn(newTurn);
        when(persistence.updateTurn(any(), any())).thenReturn(newTurn);

        Turn nextTurn = service.endTurn(currentTurn);
        assertNotNull(nextTurn);
        assertEquals(p3, nextTurn.getPlayer());
    }

    @Test
    void updateTurnPhase_shouldAdvanceCorrectly() {
        Turn turn = new Turn();
        turn.setTurnId(1L);
        turn.setTurnPhase(TurnPhase.DEPLOYMENT);
        Player player = createPlayer(1L, 1, false);
        turn.setPlayer(player);

        TerritoryState ts = createTerritoryState(1L, 1L, 1L, 3);
        player.getTerritories().add(ts);

        when(persistence.updateTurn(any(), any())).thenReturn(turn);

        Turn updated = service.updateTurnPhase(turn);
        assertNotNull(updated);
    }

    @Test
    void getPossibleAttackers_shouldReturnCorrectTerritories() {
        Player player = createPlayer(1L, 1, false);
        Turn turn = new Turn();
        turn.setPlayer(player);

        TerritoryState ts = createTerritoryState(1L, 1L, 1L, 3);
        player.getTerritories().add(ts);

        when(territoryService.getBorderingCountries(any())).thenReturn(List.of(ts.getTerritory()));
        when(gamePersistenceService.getGameById(any())).thenReturn(mockGame(UUID.randomUUID(), List.of(player)));

        List<TerritoryState> result = service.getPossibleAttackers(turn);
        assertEquals(0, result.size()); // No enemigos
    }

    @Test
    void getEnemyTerritories_shouldReturnEnemies() {
        TerritoryState ts = createTerritoryState(1L, 1L, 1L, 3);

        Player enemy = createPlayer(2L, 2, false);
        TerritoryState enemyTs = createTerritoryState(2L, 2L, 2L, 2);
        enemy.setTerritories(List.of(enemyTs));

        Game game = mockGame(UUID.randomUUID(), List.of(createPlayer(1L, 1, false), enemy));

        Turn turn = new Turn();
        turn.setPlayer(game.getPlayers().get(0));
        turn.setGameId(game.getGameId());

        when(territoryService.getBorderingCountries(any())).thenReturn(List.of(enemyTs.getTerritory()));
        when(gamePersistenceService.getGameById(any())).thenReturn(game);

        List<TerritoryState> result = service.getEnemyTerritories(turn, ts);
        assertEquals(1, result.size());
    }

    @Test
    void checkObjectiveStates_shouldSetWinner() {
        Player player = createPlayer(1L, 1, false);
        Objective obj = new Objective();
        ObjectiveType type = new ObjectiveType();
        type.setName("Conquest");
        obj.setObjectiveType(type);
        obj.setFirstRegionTerritoriesNeeded(1);
        obj.setSecondRegionTerritoriesNeeded(0);
        obj.setThirdRegionTerritoriesNeeded(0);
        obj.setFourthRegionTerritoriesNeeded(0);
        obj.setFifthRegionTerritoriesNeeded(0);
        obj.setSixthRegionTerritoriesNeeded(0);
        player.setObjective(obj);

        player.getTerritories().add(createTerritoryState(1L, 1L, 1L, 3));
        Game game = mockGame(UUID.randomUUID(), List.of(player));

        Turn turn = new Turn();
        turn.setPlayer(player);
        turn.setGameId(game.getGameId());
        turn.setTurnId(1L);

        when(gamePersistenceService.getGameById(any())).thenReturn(game);
        when(persistence.updateTurn(any(), any())).thenReturn(turn);

        Turn result = service.checkObjectiveStates(turn);
        verify(playerPersistence).updatePlayer(eq(player.getPlayerId()), any());
        assertNotNull(result);
    }

    @Test
    void checkObjectiveStates_shouldThrowWhenPlayerIsNull() {
        Game game = mockGame(UUID.randomUUID(), List.of());
        Turn turn = new Turn();
        turn.setPlayer(createPlayer(999L, 1, false));
        turn.setGameId(game.getGameId());

        when(gamePersistenceService.getGameById(any())).thenReturn(game);

        assertThrows(RuntimeException.class, () -> service.checkObjectiveStates(turn));
    }

    @Test
    void getOwnBorderingTerritories_shouldReturnOnlyOwnBorderingTerritories() {
        Turn turn = new Turn();
        Player player = new Player();
        turn.setPlayer(player);
        player.setTerritories(new ArrayList<>());

        Territory territory1 = new Territory();
        territory1.setTerritoryId(1L);

        Territory territory2 = new Territory();
        territory2.setTerritoryId(2L);

        Territory territory3 = new Territory();
        territory3.setTerritoryId(3L);

        Territory territory4 = new Territory();
        territory4.setTerritoryId(4L);

        Territory territory5 = new Territory();
        territory5.setTerritoryId(5L);

        TerritoryState playerTerritory1 = new TerritoryState();
        playerTerritory1.setTerritory(territory2);

        TerritoryState playerTerritory2 = new TerritoryState();
        playerTerritory2.setTerritory(territory3);

        TerritoryState playerTerritory3 = new TerritoryState();
        playerTerritory3.setTerritory(territory5);

        player.getTerritories().addAll(List.of(playerTerritory1, playerTerritory2, playerTerritory3));

        TerritoryState targetTerritory = new TerritoryState();
        targetTerritory.setTerritory(territory1);

        List<Territory> borderingTerritories = List.of(territory2, territory3, territory4);
        when(territoryService.getBorderingCountries(1L)).thenReturn(borderingTerritories);

        List<TerritoryState> result = service.getOwnBorderingTerritories(turn, targetTerritory);

        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(playerTerritory1, playerTerritory2)));
        assertFalse(result.contains(playerTerritory3)); // territory5 is not bordering territory1

        verify(territoryService).getBorderingCountries(1L);
    }

    @Test
    void getOwnBorderingTerritories_shouldReturnEmptyListWhenNoBorderingTerritoriesOwned() {
        Turn turn = new Turn();
        Player player = new Player();
        player.setTerritories(new ArrayList<>());
        turn.setPlayer(player);

        Territory territory1 = new Territory();
        territory1.setTerritoryId(1L);

        Territory territory2 = new Territory();
        territory2.setTerritoryId(2L);

        Territory territory3 = new Territory();
        territory3.setTerritoryId(3L);

        TerritoryState playerTerritory = new TerritoryState();
        playerTerritory.setTerritory(territory3);
        player.getTerritories().add(playerTerritory);

        TerritoryState targetTerritory = new TerritoryState();
        targetTerritory.setTerritory(territory1);

        when(territoryService.getBorderingCountries(1L)).thenReturn(List.of(territory2));

        List<TerritoryState> result = service.getOwnBorderingTerritories(turn, targetTerritory);

        assertTrue(result.isEmpty());
        verify(territoryService).getBorderingCountries(1L);
    }

    @Test
    void getOwnBorderingTerritories_shouldReturnEmptyListWhenPlayerHasNoTerritories() {
        Turn turn = new Turn();
        Player player = new Player();
        turn.setPlayer(player);
        player.setTerritories(new ArrayList<>());

        Territory territory1 = new Territory();
        territory1.setTerritoryId(1L);

        Territory territory2 = new Territory();
        territory2.setTerritoryId(2L);

        TerritoryState targetTerritory = new TerritoryState();
        targetTerritory.setTerritory(territory1);

        when(territoryService.getBorderingCountries(1L)).thenReturn(List.of(territory2));

        // Act
        List<TerritoryState> result = service.getOwnBorderingTerritories(turn, targetTerritory);

        // Assert
        assertTrue(result.isEmpty());
        verify(territoryService).getBorderingCountries(1L);
    }

    @Test
    void getOwnBorderingTerritories_shouldReturnEmptyListWhenNoBorderingTerritories() {
        // Arrange
        Turn turn = new Turn();
        Player player = new Player();
        turn.setPlayer(player);
        player.setTerritories(new ArrayList<>());

        Territory territory1 = new Territory();
        territory1.setTerritoryId(1L);

        TerritoryState playerTerritory = new TerritoryState();
        playerTerritory.setTerritory(territory1);
        player.getTerritories().add(playerTerritory);

        TerritoryState targetTerritory = new TerritoryState();
        targetTerritory.setTerritory(territory1);

        // Target territory has no bordering territories
        when(territoryService.getBorderingCountries(1L)).thenReturn(Collections.emptyList());

        // Act
        List<TerritoryState> result = service.getOwnBorderingTerritories(turn, targetTerritory);

        // Assert
        assertTrue(result.isEmpty());
        verify(territoryService).getBorderingCountries(1L);
    }

    @Test
    void getOwnBorderingTerritories_shouldNotReturnDuplicates() {
        // Arrange
        Turn turn = new Turn();
        Player player = new Player();
        turn.setPlayer(player);
        player.setTerritories(new ArrayList<>());

        Territory territory1 = new Territory();
        territory1.setTerritoryId(1L);

        Territory territory2 = new Territory();
        territory2.setTerritoryId(2L);

        // Create the same territory state that borders target
        TerritoryState playerTerritory = new TerritoryState();
        playerTerritory.setTerritory(territory2);
        player.getTerritories().add(playerTerritory);

        TerritoryState targetTerritory = new TerritoryState();
        targetTerritory.setTerritory(territory1);

        // Mock returns the same territory multiple times (edge case)
        when(territoryService.getBorderingCountries(1L)).thenReturn(List.of(territory2));

        // Act
        List<TerritoryState> result = service.getOwnBorderingTerritories(turn, targetTerritory);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(playerTerritory));
        verify(territoryService).getBorderingCountries(1L);
    }

    @Test
    void getPossibleFortifiers_shouldReturnTerritoriesWithEnoughArmiesAndBorderingTerritories() {
        // Arrange
        Turn turn = new Turn();
        Player player = new Player();
        turn.setPlayer(player);
        player.setTerritories(new ArrayList<>());

        // Create territories
        Territory territory1 = new Territory();
        territory1.setTerritoryId(1L);

        Territory territory2 = new Territory();
        territory2.setTerritoryId(2L);

        Territory territory3 = new Territory();
        territory3.setTerritoryId(3L);

        // Create territory states with different army counts
        TerritoryState validFortifier1 = new TerritoryState();
        validFortifier1.setTerritory(territory1);
        validFortifier1.setArmyCount(3); // >= 2 armies

        TerritoryState validFortifier2 = new TerritoryState();
        validFortifier2.setTerritory(territory2);
        validFortifier2.setArmyCount(5); // >= 2 armies

        TerritoryState insufficientArmies = new TerritoryState();
        insufficientArmies.setTerritory(territory3);
        insufficientArmies.setArmyCount(1); // < 2 armies

        player.getTerritories().addAll(List.of(validFortifier1, validFortifier2, insufficientArmies));

        // Mock getOwnBorderingTerritories behavior
        when(service.getOwnBorderingTerritories(turn, validFortifier1))
                .thenReturn(List.of(validFortifier2));
        when(service.getOwnBorderingTerritories(turn, validFortifier2))
                .thenReturn(List.of(validFortifier1));
        when(service.getOwnBorderingTerritories(turn, insufficientArmies))
                .thenReturn(List.of(validFortifier1));

        // Act
        List<TerritoryState> result = service.getPossibleFortifiers(turn);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(validFortifier1, validFortifier2)));
        assertFalse(result.contains(insufficientArmies));

        verify(service).getOwnBorderingTerritories(turn, validFortifier1);
        verify(service).getOwnBorderingTerritories(turn, validFortifier2);
        verify(service).getOwnBorderingTerritories(turn, insufficientArmies);
    }

    @Test
    void getPossibleFortifiers_shouldExcludeTerritoriesWithInsufficientArmies() {
        // Arrange
        Turn turn = new Turn();
        Player player = new Player();
        turn.setPlayer(player);
        player.setTerritories(new ArrayList<>());

        Territory territory1 = new Territory();
        territory1.setTerritoryId(1L);

        Territory territory2 = new Territory();
        territory2.setTerritoryId(2L);

        // Both territories have insufficient armies
        TerritoryState territory1State = new TerritoryState();
        territory1State.setTerritory(territory1);
        territory1State.setArmyCount(1);

        TerritoryState territory2State = new TerritoryState();
        territory2State.setTerritory(territory2);
        territory2State.setArmyCount(0);

        player.getTerritories().addAll(List.of(territory1State, territory2State));

        // Mock - these won't be called since territories are filtered out by army count first

        // Act
        List<TerritoryState> result = service.getPossibleFortifiers(turn);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getPossibleFortifiers_shouldExcludeTerritoriesWithNoBorderingTerritories() {
        // Arrange
        Turn turn = new Turn();
        Player player = new Player();
        turn.setPlayer(player);
        player.setTerritories(new ArrayList<>());

        Territory territory1 = new Territory();
        territory1.setTerritoryId(1L);

        Territory territory2 = new Territory();
        territory2.setTerritoryId(2L);

        // Both territories have enough armies
        TerritoryState isolatedTerritory1 = new TerritoryState();
        isolatedTerritory1.setTerritory(territory1);
        isolatedTerritory1.setArmyCount(3);

        TerritoryState isolatedTerritory2 = new TerritoryState();
        isolatedTerritory2.setTerritory(territory2);
        isolatedTerritory2.setArmyCount(2);

        player.getTerritories().addAll(List.of(isolatedTerritory1, isolatedTerritory2));

        // Mock - no bordering territories
        when(service.getOwnBorderingTerritories(turn, isolatedTerritory1))
                .thenReturn(Collections.emptyList());
        when(service.getOwnBorderingTerritories(turn, isolatedTerritory2))
                .thenReturn(Collections.emptyList());

        // Act
        List<TerritoryState> result = service.getPossibleFortifiers(turn);

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void getPossibleFortifiers_shouldReturnEmptyListWhenPlayerHasNoTerritories() {
        // Arrange
        Turn turn = new Turn();
        Player player = new Player();
        turn.setPlayer(player);
        player.setTerritories(new ArrayList<>());

        // Act
        List<TerritoryState> result = service.getPossibleFortifiers(turn);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getPossibleFortifiers_shouldHandleMixedScenarios() {
        // Arrange
        Turn turn = new Turn();
        Player player = new Player();
        turn.setPlayer(player);
        player.setTerritories(new ArrayList<>());

        Territory territory1 = new Territory();
        territory1.setTerritoryId(1L);

        Territory territory2 = new Territory();
        territory2.setTerritoryId(2L);

        Territory territory3 = new Territory();
        territory3.setTerritoryId(3L);

        Territory territory4 = new Territory();
        territory4.setTerritoryId(4L);

        // Valid fortifier: enough armies and has bordering territories
        TerritoryState validFortifier = new TerritoryState();
        validFortifier.setTerritory(territory1);
        validFortifier.setArmyCount(4);

        // Invalid: not enough armies
        TerritoryState insufficientArmies = new TerritoryState();
        insufficientArmies.setTerritory(territory2);
        insufficientArmies.setArmyCount(1);

        // Valid armies but no bordering territories
        TerritoryState noBorders = new TerritoryState();
        noBorders.setTerritory(territory3);
        noBorders.setArmyCount(3);

        // Edge case: exactly 2 armies and has borders
        TerritoryState exactlyTwoArmies = new TerritoryState();
        exactlyTwoArmies.setTerritory(territory4);
        exactlyTwoArmies.setArmyCount(2);

        player.getTerritories().addAll(List.of(validFortifier, insufficientArmies, noBorders, exactlyTwoArmies));

        // Mock behaviors
        when(service.getOwnBorderingTerritories(turn, validFortifier))
                .thenReturn(List.of(exactlyTwoArmies));
        when(service.getOwnBorderingTerritories(turn, noBorders))
                .thenReturn(Collections.emptyList());
        when(service.getOwnBorderingTerritories(turn, exactlyTwoArmies))
                .thenReturn(List.of(validFortifier));

        // Act
        List<TerritoryState> result = service.getPossibleFortifiers(turn);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(validFortifier, exactlyTwoArmies)));
        assertFalse(result.contains(insufficientArmies));
        assertFalse(result.contains(noBorders));

        // Verify interactions
        verify(service).getOwnBorderingTerritories(turn, validFortifier);
        verify(service).getOwnBorderingTerritories(turn, noBorders);
        verify(service).getOwnBorderingTerritories(turn, exactlyTwoArmies);
        verify(service, never()).getOwnBorderingTerritories(turn, insufficientArmies);
    }

    @Test
    void getPossibleFortifiers_shouldNotModifyOriginalPlayerTerritories() {
        // Arrange
        Turn turn = new Turn();
        Player player = new Player();
        turn.setPlayer(player);
        player.setTerritories(new ArrayList<>());
        Territory territory1 = new Territory();
        territory1.setTerritoryId(1L);

        TerritoryState territoryState = new TerritoryState();
        territoryState.setTerritory(territory1);
        territoryState.setArmyCount(1); // Will be filtered out

        player.getTerritories().add(territoryState);

        int originalSize = player.getTerritories().size();

        // Act
        List<TerritoryState> result = service.getPossibleFortifiers(turn);

        // Assert
        assertTrue(result.isEmpty());
        assertEquals(originalSize, player.getTerritories().size());
        assertTrue(player.getTerritories().contains(territoryState));
    }

    @Test
    void attackOutcome_shouldConquerTerritory_whenDefenderArmyReachesZero() {
        Turn turn = new Turn();
        turn.setTurnId(1L);
        Movement movement = new Movement();
        movement.setArmyCount(3);
        turn.setMovements(new ArrayList<>(List.of(movement)));

        TerritoryState attacker = new TerritoryState();
        attacker.setArmyCount(4);
        attacker.setPlayerId(10L);

        TerritoryState target = new TerritoryState();
        target.setArmyCount(1);
        target.setPlayerId(20L);

        List<Dice> diceList = List.of(new Dice(1L,6, 1)); // atacante gana

        Mockito.when(diceService.generateDiceResults(movement, 3, 1)).thenReturn(diceList);
        Mockito.when(persistence.updateTurn(1L, turn)).thenReturn(turn);

        boolean conquered = service.attackOutcome(turn, attacker, target);

        assertTrue(conquered);
        assertEquals(0, target.getArmyCount());
        assertEquals(10L, target.getPlayerId());
    }

    @Test
    void attackOutcome_shouldNotConquer_whenDefenderSurvives() {
        Turn turn = new Turn();
        turn.setTurnId(2L);
        Movement movement = new Movement();
        movement.setArmyCount(3);
        turn.setMovements(new ArrayList<>(List.of(movement)));

        TerritoryState attacker = new TerritoryState();
        attacker.setArmyCount(4);
        attacker.setPlayerId(10L);

        TerritoryState target = new TerritoryState();
        target.setArmyCount(2);
        target.setPlayerId(20L);

        List<Dice> diceList = List.of(new Dice(1L,3, 5)); // defensor gana

        Mockito.when(diceService.generateDiceResults(movement, 3, 2)).thenReturn(diceList);
        Mockito.when(persistence.updateTurn(2L, turn)).thenReturn(turn);

        boolean conquered = service.attackOutcome(turn, attacker, target);

        assertFalse(conquered);
    }

    @Test
    void attackOutcome_shouldBreakLoopIfDiceZero() {
        Turn turn = new Turn();
        turn.setTurnId(3L);
        Movement movement = new Movement();
        movement.setArmyCount(3);
        turn.setMovements(new ArrayList<>(List.of(movement)));

        TerritoryState attacker = new TerritoryState();
        attacker.setArmyCount(5);
        attacker.setPlayerId(10L);

        TerritoryState target = new TerritoryState();
        target.setArmyCount(3);
        target.setPlayerId(20L);

        List<Dice> diceList = List.of(new Dice(1L, 0, 5))
; // dado atacante 0 → se rompe el bucle

        Mockito.when(diceService.generateDiceResults(movement, 3, 3)).thenReturn(diceList);
        Mockito.when(persistence.updateTurn(3L, turn)).thenReturn(turn);

        boolean conquered = service.attackOutcome(turn, attacker, target);

        assertFalse(conquered);
        assertEquals(5, attacker.getArmyCount()); // sin cambios
        assertEquals(3, target.getArmyCount());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenPlayerIdOrTurnIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> service.didPlayerConquerThisTurn(null, 1L));
        assertThrows(IllegalArgumentException.class, () -> service.didPlayerConquerThisTurn(1L, null));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenTurnIsNull() {
        when(persistence.findById(1L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> service.didPlayerConquerThisTurn(1L, 1L));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenTurnNotBelongsToPlayer() {
        Long playerId = 1L;
        Long turnId = 1L;

        Player otherPlayer = new Player();
        otherPlayer.setPlayerId(999L);

        Turn turn = new Turn();
        turn.setPlayer(otherPlayer);

        when(persistence.findById(turnId)).thenReturn(turn);

        assertThrows(IllegalStateException.class, () -> service.didPlayerConquerThisTurn(playerId, turnId));
    }

    @Test
    void shouldReturnFalseWhenMovementsIsNullOrEmpty() {
        Long playerId = 1L;
        Long turnId = 1L;

        Player player = new Player();
        player.setPlayerId(playerId);

        Turn turn = new Turn();
        turn.setPlayer(player);
        turn.setMovements(null);

        when(persistence.findById(turnId)).thenReturn(turn);

        assertFalse(service.didPlayerConquerThisTurn(playerId, turnId));

        turn.setMovements(new ArrayList<>());
        assertFalse(service.didPlayerConquerThisTurn(playerId, turnId));
    }

    @Test
    void shouldReturnTrueWhenTerritoryWasConquered() {
        Long playerId = 1L;
        Long turnId = 1L;
        Long territoryId = 100L;

        Player player = new Player();
        player.setPlayerId(playerId);

        Movement movement = new Movement();
        movement.setEndTerritoryId(territoryId);
        movement.setMovementType(TurnPhase.ATTACK);

        Turn turn = new Turn();
        turn.setPlayer(player);
        turn.setMovements(List.of(movement));

        TerritoryState conqueredTerritory = new TerritoryState();
        conqueredTerritory.setPlayerId(playerId);

        when(persistence.findById(turnId)).thenReturn(turn);
        when(territoryStateService.findByIdModel(territoryId)).thenReturn(conqueredTerritory);

        assertTrue(service.didPlayerConquerThisTurn(playerId, turnId));
    }

    @Test
    void shouldReturnFalseWhenNoTerritoryWasConquered() {
        Long playerId = 1L;
        Long turnId = 1L;
        Long territoryId = 100L;

        Player player = new Player();
        player.setPlayerId(playerId);

        Movement movement = new Movement();
        movement.setEndTerritoryId(territoryId);
        movement.setMovementType(TurnPhase.ATTACK);

        Turn turn = new Turn();
        turn.setPlayer(player);
        turn.setMovements(List.of(movement));

        TerritoryState enemyTerritory = new TerritoryState();
        enemyTerritory.setPlayerId(999L);

        when(persistence.findById(turnId)).thenReturn(turn);
        when(territoryStateService.findByIdModel(territoryId)).thenReturn(enemyTerritory);

        assertFalse(service.didPlayerConquerThisTurn(playerId, turnId));
    }

}
