package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.CardStateEntity;
import ar.edu.utn.frc.tup.piii.models.CardState;
import ar.edu.utn.frc.tup.piii.models.Symbol;
import ar.edu.utn.frc.tup.piii.repository.jpa.CardStateJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardStatePersistence {
    private final CardStateJpaRepository cardStateJpaRepository;
    private final ModelMapper modelMapper;

    public CardState findCardStateById(Long cardStateId) {
        return modelMapper.map(cardStateJpaRepository.getReferenceById(cardStateId), CardState.class);
    }
    public List<CardState> findCardStateByGame(UUID gameId) {
        return cardStateJpaRepository.getAllByGame_GameId(gameId)
                .stream()
                .map(entity -> modelMapper.map(entity, CardState.class))
                .collect(Collectors.toList());
    }

    public List<CardState> findCardsByPlayer(Long playerId) {
        return cardStateJpaRepository.findCardStateByPlayer_PlayerId(playerId)
                .stream()
                .map(entity -> modelMapper.map(entity, CardState.class))
                .collect(Collectors.toList());
    }

    public Map<Symbol, List<CardState>> findCardsBySymbol(List<CardState> cardsInHand) {
        return cardsInHand.stream()
                .collect(Collectors.groupingBy(cardState -> cardState.getCard().getSymbol()));
    }

    public CardState findAvailableConquestCard() {
        List<CardStateEntity> availableEntities = cardStateJpaRepository.findByPlayerIsNull();

        if (availableEntities.isEmpty()) {
            return null;
        }

        return modelMapper.map(availableEntities.get(0), CardState.class);
    }


    public CardState save(CardState cardState) {
        CardStateEntity cardStateEntity = modelMapper.map(cardState, CardStateEntity.class);
        return modelMapper.map(cardStateJpaRepository.save(cardStateEntity), CardState.class);
    }
}
