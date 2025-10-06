package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.*;
import ar.edu.utn.frc.tup.piii.enums.Color;
import ar.edu.utn.frc.tup.piii.enums.BotDifficulty;
import ar.edu.utn.frc.tup.piii.enums.PlayerType;
import ar.edu.utn.frc.tup.piii.services.CardStateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

@WebMvcTest(CardStateController.class)
class CardStateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardStateService cardStateService;

    @Autowired
    private ObjectMapper objectMapper;

    private CardStateDto createCardStateDto() {
        SymbolDto symbol = new SymbolDto();
        symbol.setSymbolId(1L);
        symbol.setSymbol("CANNON");

        TerritoryDto territory = new TerritoryDto();
        territory.setTerritoryId(1L);
        territory.setName("Argentina");
        territory.setRegion(null);
        territory.setBordersId(List.of());

        CardDto card = new CardDto();
        card.setCardId(1L);
        card.setTerritory(territory);
        card.setSymbol(symbol);

        CardStateDto dto = new CardStateDto();
        dto.setCardStateId(1L);
        dto.setCard(card);
        return dto;
    }

    private CardDto createCardDto() {
        SymbolDto symbol = new SymbolDto();
        symbol.setSymbolId(1L);
        symbol.setSymbol("CANNON");

        TerritoryDto territory = new TerritoryDto();
        territory.setTerritoryId(1L);
        territory.setName("Argentina");
        territory.setRegion(null);
        territory.setBordersId(List.of());

        CardDto dto = new CardDto();
        dto.setCardId(1L);
        dto.setTerritory(territory);
        dto.setSymbol(symbol);
        return dto;
    }

    private PlayerDto createPlayerDto() {
        PlayerDto dto = new PlayerDto();
        dto.setPlayerId(1L);
        dto.setUser(null);
        dto.setGameId(UUID.randomUUID());
        dto.setObjective(null);
        dto.setPlayerColor(Color.YELLOW);
        dto.setTurnOrder(1);
        dto.setWinner(false);
        dto.setHasLost(false);
        dto.setDifficulty(BotDifficulty.EASY);
        dto.setPlayerType(PlayerType.BOT);
        dto.setTerritories(List.of());
        return dto;
    }

    private TurnDto createTurnDto() {
        TurnDto dto = new TurnDto();
        dto.setTurnId(1L);
        return dto;
    }

    @Test
    @DisplayName("GET /cardStates/game/{gameId} - OK")
    void testGetAllByGame_OK() throws Exception {
        UUID gameId = UUID.randomUUID();
        List<CardStateDto> cardStates = List.of(createCardStateDto());

        when(cardStateService.getAllByGame(gameId)).thenReturn(cardStates);

        mockMvc.perform(get("/cardStates/game/" + gameId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cardStates)));
    }

    @Test
    @DisplayName("GET /cardStates/game/{gameId} - NOT_FOUND")
    void testGetAllByGame_Empty() throws Exception {
        UUID gameId = UUID.randomUUID();

        when(cardStateService.getAllByGame(gameId)).thenReturn(List.of());

        mockMvc.perform(get("/cardStates/game/" + gameId))
                .andExpect(status().isNotFound())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("GET /cardStates/{id} - OK")
    void testGetById_OK() throws Exception {
        CardStateDto dto = createCardStateDto();

        when(cardStateService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/cardStates/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    @DisplayName("GET /cardStates/player/{playerId} - OK")
    void testGetCardsByPlayer_OK() throws Exception {
        List<CardStateDto> cards = List.of(createCardStateDto());

        when(cardStateService.getCardsByPlayer(1L)).thenReturn(cards);

        mockMvc.perform(get("/cardStates/player/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cards)));
    }

    @Test
    @DisplayName("GET /cardStates/player/{playerId} - NOT_FOUND")
    void testGetCardsByPlayer_Empty() throws Exception {
        when(cardStateService.getCardsByPlayer(1L)).thenReturn(List.of());

        mockMvc.perform(get("/cardStates/player/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("PATCH /cardStates/trade/{playerId} - valid")
    void testValidTrade_Valid() throws Exception {
        when(cardStateService.validTrade(argThat(dto -> dto.getPlayerId() == 1L))).thenReturn(true);
        
        mockMvc.perform(patch("/cardStates/trade/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("El intercambio es válido y puede ser realizado."));
    }

    @Test
    @DisplayName("PATCH /cardStates/trade/{playerId} - invalid")
    void testValidTrade_Invalid() throws Exception {
        when(cardStateService.validTrade(createPlayerDto())).thenReturn(false);

        mockMvc.perform(patch("/cardStates/trade/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El intercambio no es válido. El jugador no tiene 3 cartas del mismo símbolo."));
    }

    @Test
    @DisplayName("POST /cardStates/conquest")
    void testDealConquestCard() throws Exception {
        ConquestCardRequestDto request = new ConquestCardRequestDto(createPlayerDto(), createTurnDto());
        CardStateDto result = createCardStateDto();

        when(cardStateService.dealConquestCard(request.getPlayer(), request.getTurn())).thenReturn(result);

        mockMvc.perform(post("/cardStates/conquest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }

    @Test
    @DisplayName("POST /cardStates/returnCard")
    void testReturnCardToDeck() throws Exception {
        CardDto cardDto = createCardDto();

        when(cardStateService.returnCardToDeck(cardDto)).thenReturn(cardDto);

        mockMvc.perform(post("/cardStates/returnCard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cardDto)));
    }
}
