package ar.edu.utn.frc.tup.piii.services.implementations;

import ar.edu.utn.frc.tup.piii.dtos.TerritoryStateDto;
import ar.edu.utn.frc.tup.piii.enums.TurnPhase;
import ar.edu.utn.frc.tup.piii.models.*;
import ar.edu.utn.frc.tup.piii.persistence.TurnPersistence;
import ar.edu.utn.frc.tup.piii.services.TerritoryStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TurnBotEasyImplTest {

    @Mock
    private TerritoryStateService territoryStateService;

    @Mock
    private TurnServiceCommon turnServiceCommon;

    @Mock
    private TurnPersistence turnPersistence;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TurnBotEasyImpl turnBotEasyImpl;

    private Turn baseTurnWithPhase(TurnPhase phase) {
        Turn turn = new Turn();
        turn.setTurnPhase(phase);
        Player player = new Player();
        player.setPlayerId(1L);
        turn.setPlayer(player);
        turn.setMovements(new ArrayList<>(List.of(new Movement())));
        return turn;
    }

    @Test
    void playDeployStage_valid() throws Exception {
        Turn turn = baseTurnWithPhase(TurnPhase.DEPLOYMENT);
        Movement first = new Movement();
        first.setArmyCount(5);
        turn.setMovements(new ArrayList<>(List.of(first)));

        TerritoryState ts = new TerritoryState();
        ts.setTerritoryStateId(1L);
        ts.setPlayerId(1L);
        ts.setArmyCount(2);
        turn.getPlayer().setTerritories(List.of(ts));

        when(turnPersistence.updateTurn(any(), any())).thenReturn(turn);
        when(modelMapper.map(any(), eq(TerritoryStateDto.class))).thenReturn(new TerritoryStateDto());

        Turn result = turnBotEasyImpl.playDeployStage(turn);

        assertNotNull(result);
        verify(territoryStateService).update(eq(1L), any());
    }

    @Test
    void playDeployStage_shouldEndEarlyTurn() throws Exception {
        Turn turn = baseTurnWithPhase(TurnPhase.DEPLOYMENT);
        Movement first = new Movement();
        first.setArmyCount(0);
        turn.setMovements(new ArrayList<>(List.of(first)));

        when(turnPersistence.findByPlayer(any())).thenReturn(List.of(new Turn()));
        when(turnServiceCommon.updateTurnPhase(any())).thenReturn(turn);

        Turn result = turnBotEasyImpl.playDeployStage(turn);
        assertEquals(turn, result);
    }

    @Test
    void playDeployStage_invalidPhase() {
        Turn turn = baseTurnWithPhase(TurnPhase.ATTACK);
        assertThrows(Exception.class, () -> turnBotEasyImpl.playDeployStage(turn));
    }

    @Test
    void playAttackStage_valid() throws Exception {
        Turn turn = baseTurnWithPhase(TurnPhase.ATTACK);
        turn.setMovements(new ArrayList<>());
        TerritoryState attacker = new TerritoryState();
        attacker.setTerritoryStateId(1L);
        attacker.setArmyCount(5);
        attacker.setPlayerId(1L);

        TerritoryState target = new TerritoryState();
        target.setTerritoryStateId(2L);
        target.setArmyCount(2);
        target.setPlayerId(2L);

        when(turnServiceCommon.getPossibleAttackers(turn)).thenReturn(List.of(attacker));
        when(turnServiceCommon.getEnemyTerritories(turn, attacker)).thenReturn(List.of(target));
        when(turnServiceCommon.attackOutcome(turn, attacker, target)).thenReturn(true);
        when(turnPersistence.updateTurn(any(), any())).thenReturn(turn);

        Turn result = turnBotEasyImpl.playAttackStage(turn);
        assertEquals(turn, result);
        verify(territoryStateService, times(1)).updateModel(eq(1L), eq(attacker));
        verify(territoryStateService, times(1)).updateModel(eq(2L), eq(target));
    }

    @Test
    void playAttackStage_noTargets() throws Exception {
        Turn turn = baseTurnWithPhase(TurnPhase.ATTACK);
        when(turnServiceCommon.getPossibleAttackers(turn)).thenReturn(List.of());

        when(turnServiceCommon.updateTurnPhase(any())).thenReturn(turn);
        Turn result = turnBotEasyImpl.playAttackStage(turn);

        assertEquals(turn, result);
    }

    @Test
    void playAttackStage_invalidPhase() {
        Turn turn = baseTurnWithPhase(TurnPhase.DEPLOYMENT);
        assertThrows(Exception.class, () -> turnBotEasyImpl.playAttackStage(turn));
    }

    @Test
    void playFortifyStage_valid() throws Exception {
        Turn turn = baseTurnWithPhase(TurnPhase.FORTIFY);
        turn.setMovements(new ArrayList<>());

        TerritoryState from = new TerritoryState();
        from.setTerritoryStateId(1L);
        from.setArmyCount(4);

        TerritoryState to = new TerritoryState();
        to.setTerritoryStateId(2L);
        to.setArmyCount(2);

        when(turnServiceCommon.getPossibleFortifiers(turn)).thenReturn(List.of(from));
        when(turnServiceCommon.getOwnBorderingTerritories(turn, from)).thenReturn(List.of(to));
        when(turnPersistence.updateTurn(any(), any())).thenReturn(turn);

        Turn result = turnBotEasyImpl.playFortifyStage(turn);
        assertEquals(turn, result);
    }

    @Test
    void playFortifyStage_noValidTerritories() throws Exception {
        Turn turn = baseTurnWithPhase(TurnPhase.FORTIFY);
        turn.setMovements(new ArrayList<>());
        when(turnServiceCommon.getPossibleFortifiers(turn)).thenReturn(List.of());

        when(turnServiceCommon.updateTurnPhase(any())).thenReturn(turn);
        Turn result = turnBotEasyImpl.playFortifyStage(turn);
        assertEquals(turn, result);
    }

    @Test
    void playFortifyStage_invalidPhase() {
        Turn turn = baseTurnWithPhase(TurnPhase.DEPLOYMENT);
        assertThrows(Exception.class, () -> turnBotEasyImpl.playFortifyStage(turn));
    }

    @Test
    void playCardStage_valid() {
        Turn turn = baseTurnWithPhase(TurnPhase.CARD);
        when(turnServiceCommon.updateTurnPhase(any())).thenReturn(turn);
        Turn result = turnBotEasyImpl.playCardStage(turn);
        assertEquals(turn, result);
    }
}