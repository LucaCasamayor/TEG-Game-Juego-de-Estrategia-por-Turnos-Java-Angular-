package ar.edu.utn.frc.tup.piii.persistence;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import ar.edu.utn.frc.tup.piii.entities.CardEntity;
import ar.edu.utn.frc.tup.piii.models.Card;
import ar.edu.utn.frc.tup.piii.repository.jpa.CardJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class CardPersistenceTest {

    @Mock
    private CardJpaRepository cardRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CardPersistence cardPersistence;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllCards() {
        CardEntity entity1 = new CardEntity();
        CardEntity entity2 = new CardEntity();
        Card card1 = new Card();
        Card card2 = new Card();

        when(cardRepository.findAll()).thenReturn(List.of(entity1, entity2));
        when(modelMapper.map(entity1, Card.class)).thenReturn(card1);
        when(modelMapper.map(entity2, Card.class)).thenReturn(card2);

        List<Card> cards = cardPersistence.findAllCards();

        assertEquals(2, cards.size());
        assertEquals(card1, cards.get(0));
        assertEquals(card2, cards.get(1));
        verify(cardRepository).findAll();
    }

    @Test
    void findCardById() {
        Long id = 1L;
        CardEntity entity = new CardEntity();
        Card expectedCard = new Card();

        when(cardRepository.getReferenceById(id)).thenReturn(entity);
        when(modelMapper.map(entity, Card.class)).thenReturn(expectedCard);

        Card result = cardPersistence.findCardById(id);

        assertEquals(expectedCard, result);
        verify(cardRepository).getReferenceById(id);
    }
}