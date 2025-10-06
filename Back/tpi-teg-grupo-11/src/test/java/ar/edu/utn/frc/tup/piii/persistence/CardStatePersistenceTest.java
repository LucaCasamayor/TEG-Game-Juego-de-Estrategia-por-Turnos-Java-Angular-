package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.CardStateEntity;
import ar.edu.utn.frc.tup.piii.models.Card;
import ar.edu.utn.frc.tup.piii.models.CardState;
import ar.edu.utn.frc.tup.piii.models.Symbol;
import ar.edu.utn.frc.tup.piii.repository.jpa.CardStateJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class CardStatePersistenceTest {

    @Mock
    private CardStateJpaRepository cardStateJpaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CardStatePersistence cardStatePersistence;

    @Test
    void findCardStateById() {
        Long id = 1L;
        CardStateEntity entity = new CardStateEntity();
        CardState dto = new CardState();

        Mockito.when(cardStateJpaRepository.getReferenceById(id)).thenReturn(entity);
        Mockito.when(modelMapper.map(entity, CardState.class)).thenReturn(dto);

        CardState result = cardStatePersistence.findCardStateById(id);

        Assertions.assertEquals(dto, result);
    }

    @Test
    void findCardStateByGame() {
        UUID gameId = UUID.randomUUID();

        List<CardStateEntity> entities = List.of(new CardStateEntity(), new CardStateEntity());


        CardState dto1 = new CardState();
        CardState dto2 = new CardState();

        Mockito.when(cardStateJpaRepository.getAllByGame_GameId(gameId)).thenReturn(entities);
        Mockito.when(modelMapper.map(entities.get(0), CardState.class)).thenReturn(dto1);
        Mockito.when(modelMapper.map(entities.get(1), CardState.class)).thenReturn(dto2);

        List<CardState> result = cardStatePersistence.findCardStateByGame(gameId);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(dto1, result.get(0));
        Assertions.assertEquals(dto2, result.get(1));

    }

    @Test
    void findCardsByPlayer() {
        Long playerId = 1L;

        CardStateEntity entity = new CardStateEntity();
        CardState model = new CardState();

        List<CardStateEntity> entityList = List.of(entity);
        List<CardState> expected = List.of(model);

        Mockito.when(cardStateJpaRepository.findCardStateByPlayer_PlayerId(playerId)).thenReturn(entityList);
        Mockito.when(modelMapper.map(entity, CardState.class)).thenReturn(model);

        List<CardState> result = cardStatePersistence.findCardsByPlayer(playerId);

        Assertions.assertEquals(expected, result);


    }

    @Test
    void findCardsBySymbol() {
        Symbol infantry = new Symbol(1L, "Infantry", "img.png");
        Symbol cavalry = new Symbol(2L, "Cavalry", "img.png");

        CardState card1 = new CardState();
        card1.setCard(new Card(1L, null, infantry));
        CardState card2 = new CardState();
        card2.setCard(new Card(2L, null, cavalry));
        CardState card3 = new CardState();
        card3.setCard(new Card(3L, null, infantry));

        List<CardState> cards = List.of(card1, card2, card3);

        Map<Symbol, List<CardState>> grouped = cardStatePersistence.findCardsBySymbol(cards);

        Assertions.assertEquals(2, grouped.size());
        Assertions.assertEquals(2, grouped.get(infantry).size());
        Assertions.assertEquals(1, grouped.get(cavalry).size());

    }

    @Test
    void findAvailableConquestCard() {
        CardStateEntity entity = new CardStateEntity();
        CardState dto = new CardState();

        Mockito.when(cardStateJpaRepository.findByPlayerIsNull()).thenReturn(List.of(entity));
        Mockito.when(modelMapper.map(entity, CardState.class)).thenReturn(dto);

        CardState result = cardStatePersistence.findAvailableConquestCard();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(dto, result);

        Mockito.when(cardStateJpaRepository.findByPlayerIsNull()).thenReturn(Collections.emptyList());
        CardState resultEmpty = cardStatePersistence.findAvailableConquestCard();

        Assertions.assertNull(resultEmpty);
    }
    @Test
    void findAvailableConquestCard_shouldReturnNullIfEmpty() {
        Mockito.when(cardStateJpaRepository.findByPlayerIsNull()).thenReturn(Collections.emptyList());

        CardState result = cardStatePersistence.findAvailableConquestCard();

        Assertions.assertNull(result);
    }

    @Test
    void save() {
        CardState input = new CardState();
        CardStateEntity entity = new CardStateEntity();
        CardStateEntity savedEntity = new CardStateEntity();
        CardState output = new CardState();

        Mockito.when(modelMapper.map(input, CardStateEntity.class)).thenReturn(entity);
        Mockito.when(cardStateJpaRepository.save(entity)).thenReturn(savedEntity);
        Mockito.when(modelMapper.map(savedEntity, CardState.class)).thenReturn(output);

        CardState result = cardStatePersistence.save(input);

        Assertions.assertEquals(output, result);
    }
}