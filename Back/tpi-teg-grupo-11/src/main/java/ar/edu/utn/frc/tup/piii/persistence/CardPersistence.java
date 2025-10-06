package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.models.Card;
import ar.edu.utn.frc.tup.piii.repository.jpa.CardJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardPersistence {

    private final CardJpaRepository cardRepository;
    private final ModelMapper modelMapper;

    public List<Card> findAllCards() {
        return cardRepository.findAll().stream()
                .map(card -> modelMapper.map(card, Card.class))
                .collect(Collectors.toList());
    }

    public Card findCardById(Long cardId) {
        return modelMapper.map(cardRepository.getReferenceById(cardId), Card.class);
    }

}
