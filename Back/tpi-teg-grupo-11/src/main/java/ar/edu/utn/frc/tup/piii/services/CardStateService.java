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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardStateService {

    private final CardStatePersistence persistence;
    private final ModelMapper modelMapper;
    private final TurnServiceCommon turnServiceCommon;

    public List<CardStateDto> getAllByGame(UUID gameId) {
        List<CardState> cardStates = persistence.findCardStateByGame(gameId);
        return cardStates.stream()
                .map(cardState -> modelMapper.map(cardState, CardStateDto.class))
                .collect(Collectors.toList());
    }

    public CardStateDto getById(Long id) {
        CardState cardState = persistence.findCardStateById(id);
        if (cardState == null) {
            throw new RuntimeException("CardState no encontrado con ID: " + id);
        }
        return modelMapper.map(cardState, CardStateDto.class);
    }

    public List<CardStateDto> getCardsByPlayer(Long playerId) {
        List<CardState> cards = persistence.findCardsByPlayer(playerId);
        return cards.stream()
                .map(card -> modelMapper.map(card, CardStateDto.class))
                .collect(Collectors.toList());
    }

    public Map<Symbol, List<CardStateDto>> getCardsBySymbol(List<CardState> cardsInHand) {
        Map<Symbol, List<CardState>> grouped = persistence.findCardsBySymbol(cardsInHand);

        return grouped.entrySet().stream() //Itera sobre cada entrada del mapa
                .collect(Collectors.toMap(
                        Map.Entry::getKey, //Cada entrada tiene una clave y la usamos como clave del nuevo mapa
                        entry -> entry.getValue().stream()//para el valor de cada entrada
                                .map(cardState -> modelMapper.map(cardState, CardStateDto.class))
                                .collect(Collectors.toList()) //devuelve un nuevo map con simbolo de la carta y listas de cartas agrupadas por simbolo
                ));
    }

    public boolean validTrade(PlayerDto player) {
        List<CardState> cardsInHand = persistence.findCardsByPlayer(player.getPlayerId());
        Map<Symbol, List<CardState>> groupedCards = persistence.findCardsBySymbol(cardsInHand);

        List<String> requiredSymbols = Arrays.asList("CANNON", "GLOBE", "INFANTRY");

        // Verifica si el jugador tiene al menos una carta de cada símbolo requerido
        for (String symbolName : requiredSymbols) {
            boolean hasSymbol = false;
            for (Map.Entry<Symbol, List<CardState>> entry : groupedCards.entrySet()) {
                Symbol symbol = entry.getKey();
                if (symbol.getSymbol().equals(symbolName) && !entry.getValue().isEmpty()) {
                    hasSymbol = true;
                    break;
                }
            }
            if (!hasSymbol) {
                return false;
            }

            // Validar si el jugador tiene 3 o más cartas del mismo símbolo
            for (Map.Entry<Symbol, List<CardState>> entry : groupedCards.entrySet()) {
                List<CardState> cards = entry.getValue();
                if (cards.size() >= 3) {
                    return true;
                }
            }

        }

        return false;
    }

    public CardStateDto dealConquestCard(PlayerDto playerDto, TurnDto turnDto) {
        // Verificar que el jugador haya conquistado un territorio en este turno
        if (!turnServiceCommon.didPlayerConquerThisTurn(playerDto.getPlayerId(), turnDto.getTurnId())) {
            throw new IllegalStateException("El jugador no conquistó ningún territorio en este turno.");
        }

        CardState availableCard = persistence.findAvailableConquestCard();
        if (availableCard == null) {
            throw new IllegalStateException("No hay cartas de conquista disponibles para repartir.");
        }

        // Asignar la carta al jugador
        availableCard.setPlayer(modelMapper.map(playerDto, Player.class));
        availableCard.setWasUsed(true);
        CardState updatedCard = persistence.save(availableCard);

        return modelMapper.map(updatedCard, CardStateDto.class);
    }


    public CardDto returnCardToDeck(CardDto cardDto) {
        CardState cardState = persistence.findCardStateById(cardDto.getCardId());
        if (cardState == null) {
            throw new IllegalArgumentException("La carta no existe.");
        }

        // Devolver la carta al mazo
        cardState.setPlayer(null);
        cardState.setWasUsed(false);
        CardState updatedCard = persistence.save(cardState);

        return modelMapper.map(updatedCard, CardDto.class);
    }

}