package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.CardDto;
import ar.edu.utn.frc.tup.piii.dtos.CardStateDto;
import ar.edu.utn.frc.tup.piii.dtos.PlayerDto;
import ar.edu.utn.frc.tup.piii.dtos.TurnDto;
import ar.edu.utn.frc.tup.piii.models.CardState;
import ar.edu.utn.frc.tup.piii.models.Player;
import ar.edu.utn.frc.tup.piii.models.Symbol;
import ar.edu.utn.frc.tup.piii.persistence.CardStatePersistence;
import ar.edu.utn.frc.tup.piii.services.implementations.TurnServiceCommon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardStateServiceTest {

    @Mock
    private CardStatePersistence persistence;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private TurnServiceCommon turnServiceCommon;

    @InjectMocks
    private CardStateService service;

    private CardState cardState;
    private CardStateDto cardStateDto;
    private CardDto cardDto;
    private PlayerDto playerDto;
    private TurnDto turnDto;
    private Symbol cannon;

    @BeforeEach
    void setUp() {
        cardState = new CardState();
        cardState.setCardStateId(1L);
        cardState.setWasUsed(false);

        cardStateDto = new CardStateDto();
        cardStateDto.setCardStateId(1L);

        cardDto = new CardDto();
        cardDto.setCardId(1L);

        playerDto = new PlayerDto();
        playerDto.setPlayerId(99L);

        turnDto = new TurnDto();
        turnDto.setTurnId(5L);

        cannon = new Symbol();
        cannon.setSymbol("CANNON");
    }

    @Test
    void getAllByGame() {
        UUID gameId = UUID.randomUUID();
        when(persistence.findCardStateByGame(gameId)).thenReturn(List.of(cardState));
        when(modelMapper.map(cardState, CardStateDto.class)).thenReturn(cardStateDto);

        List<CardStateDto> result = service.getAllByGame(gameId);

        assertEquals(1, result.size());
        assertEquals(cardStateDto, result.get(0));
    }

    @Test
    void getById() {
        when(persistence.findCardStateById(1L)).thenReturn(cardState);
        when(modelMapper.map(cardState, CardStateDto.class)).thenReturn(cardStateDto);

        CardStateDto result = service.getById(1L);

        assertEquals(cardStateDto, result);
    }
    @Test
    void testGetById_notFound() {
        when(persistence.findCardStateById(1L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getById(1L));
        assertEquals("CardState no encontrado con ID: 1", ex.getMessage());
    }

    @Test
    void getCardsByPlayer() {
        when(persistence.findCardsByPlayer(99L)).thenReturn(List.of(cardState));
        when(modelMapper.map(cardState, CardStateDto.class)).thenReturn(cardStateDto);

        List<CardStateDto> result = service.getCardsByPlayer(99L);

        assertEquals(1, result.size());
    }

    @Test
    void getCardsBySymbol() {
        cardState.setCardStateId(1L);
        Map<Symbol, List<CardState>> grouped = Map.of(cannon, List.of(cardState));
        when(persistence.findCardsBySymbol(List.of(cardState))).thenReturn(grouped);
        when(modelMapper.map(cardState, CardStateDto.class)).thenReturn(cardStateDto);

        Map<Symbol, List<CardStateDto>> result = service.getCardsBySymbol(List.of(cardState));

        assertTrue(result.containsKey(cannon));
        assertEquals(1, result.get(cannon).size());
    }

    @Test
    void validTrade() {
        Symbol infantry = new Symbol(); infantry.setSymbol("INFANTRY");
        Symbol globe = new Symbol(); globe.setSymbol("GLOBE");

        CardState c1 = new CardState(); c1.setCardStateId(1L);
        CardState c2 = new CardState(); c2.setCardStateId(2L);
        CardState c3 = new CardState(); c3.setCardStateId(3L);

        Map<Symbol, List<CardState>> map = Map.of(
                cannon, List.of(c1),
                globe, List.of(c2),
                infantry, List.of(c3, new CardState(), new CardState()) // 3 de INFANTRY
        );

        when(persistence.findCardsByPlayer(99L)).thenReturn(List.of(c1, c2, c3));
        when(persistence.findCardsBySymbol(anyList())).thenReturn(map);

        playerDto.setPlayerId(99L);

        assertTrue(service.validTrade(playerDto));
    }
    @Test
    void testValidTrade_missingSymbol() {
        Map<Symbol, List<CardState>> incomplete = Map.of(cannon, List.of(cardState)); // Falta GLOBE e INFANTRY
        when(persistence.findCardsByPlayer(99L)).thenReturn(List.of(cardState));
        when(persistence.findCardsBySymbol(anyList())).thenReturn(incomplete);

        playerDto.setPlayerId(99L);

        assertFalse(service.validTrade(playerDto));
    }

    @Test
    void dealConquestCard() {
        when(turnServiceCommon.didPlayerConquerThisTurn(99L, 5L)).thenReturn(true);
        when(persistence.findAvailableConquestCard()).thenReturn(cardState);
        when(persistence.save(any())).thenReturn(cardState);
        when(modelMapper.map(playerDto, Player.class)).thenReturn(new Player());
        when(modelMapper.map(cardState, CardStateDto.class)).thenReturn(cardStateDto);

        CardStateDto result = service.dealConquestCard(playerDto, turnDto);

        assertEquals(cardStateDto, result);
        assertTrue(cardState.isWasUsed());
    }
    @Test
    void testDealConquestCard_noConquest() {
        when(turnServiceCommon.didPlayerConquerThisTurn(99L, 5L)).thenReturn(false);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                service.dealConquestCard(playerDto, turnDto));

        assertEquals("El jugador no conquistó ningún territorio en este turno.", ex.getMessage());
    }
    @Test
    void testDealConquestCard_noCardsAvailable() {
        when(turnServiceCommon.didPlayerConquerThisTurn(99L, 5L)).thenReturn(true);
        when(persistence.findAvailableConquestCard()).thenReturn(null);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                service.dealConquestCard(playerDto, turnDto));

        assertEquals("No hay cartas de conquista disponibles para repartir.", ex.getMessage());
    }

    @Test
    void returnCardToDeck() {
        when(persistence.findCardStateById(1L)).thenReturn(cardState);
        when(persistence.save(cardState)).thenReturn(cardState);
        when(modelMapper.map(cardState, CardDto.class)).thenReturn(cardDto);

        CardDto result = service.returnCardToDeck(cardDto);

        assertEquals(cardDto, result);
        assertNull(cardState.getPlayer());
        assertFalse(cardState.isWasUsed());
    }
    @Test
    void testReturnCardToDeck_notFound() {
        when(persistence.findCardStateById(1L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.returnCardToDeck(cardDto));

        assertEquals("La carta no existe.", ex.getMessage());
    }
}