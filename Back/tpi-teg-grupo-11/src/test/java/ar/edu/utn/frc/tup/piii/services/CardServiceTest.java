package ar.edu.utn.frc.tup.piii.services;
import ar.edu.utn.frc.tup.piii.dtos.CardDto;
import ar.edu.utn.frc.tup.piii.dtos.SymbolDto;
import ar.edu.utn.frc.tup.piii.dtos.TerritoryDto;
import ar.edu.utn.frc.tup.piii.models.Card;
import ar.edu.utn.frc.tup.piii.models.Symbol;
import ar.edu.utn.frc.tup.piii.models.Territory;
import ar.edu.utn.frc.tup.piii.persistence.CardPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardServiceTest {

    @Mock
    private CardPersistence persistence;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        // Crear modelos dominio
        Card card1 = new Card();
        card1.setCardId(1L);
        card1.setTerritory(new Territory());
        card1.setSymbol(new Symbol());

        Card card2 = new Card();
        card2.setCardId(2L);
        card2.setTerritory(new Territory());
        card2.setSymbol(new Symbol());

        // Crear DTOs esperados
        TerritoryDto territoryDto = new TerritoryDto();
        SymbolDto symbolDto = new SymbolDto();

        CardDto dto1 = new CardDto(1L, territoryDto, symbolDto);
        CardDto dto2 = new CardDto(2L, territoryDto, symbolDto);

        // Mockear llamadas
        when(persistence.findAllCards()).thenReturn(List.of(card1, card2));
        when(modelMapper.map(card1, CardDto.class)).thenReturn(dto1);
        when(modelMapper.map(card2, CardDto.class)).thenReturn(dto2);

        // Ejecutar
        List<CardDto> result = cardService.getAll();

        // Validar
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getCardId());
        assertEquals(2L, result.get(1).getCardId());
        assertNotNull(result.get(0).getTerritory());
        assertNotNull(result.get(1).getSymbol());

        verify(persistence).findAllCards();
        verify(modelMapper, times(2)).map(any(Card.class), eq(CardDto.class));
    }

    @Test
    void testGetById() {
        Long id = 42L;

        Card card = new Card();
        card.setCardId(id);
        card.setTerritory(new Territory());
        card.setSymbol(new Symbol());

        TerritoryDto territoryDto = new TerritoryDto();
        SymbolDto symbolDto = new SymbolDto();

        CardDto dto = new CardDto(id, territoryDto, symbolDto);

        when(persistence.findCardById(id)).thenReturn(card);
        when(modelMapper.map(card, CardDto.class)).thenReturn(dto);

        CardDto result = cardService.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getCardId());
        assertNotNull(result.getTerritory());
        assertNotNull(result.getSymbol());

        verify(persistence).findCardById(id);
        verify(modelMapper).map(card, CardDto.class);
    }
}
