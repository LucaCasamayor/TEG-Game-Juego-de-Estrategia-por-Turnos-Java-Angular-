package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.CardDto;
import ar.edu.utn.frc.tup.piii.dtos.CardStateDto;
import ar.edu.utn.frc.tup.piii.dtos.ConquestCardRequestDto;
import ar.edu.utn.frc.tup.piii.dtos.PlayerDto;
import ar.edu.utn.frc.tup.piii.services.CardStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cardStates")
@RequiredArgsConstructor
public class CardStateController {

    private final CardStateService cardStateService;

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<CardStateDto>> getAllByGame(@PathVariable UUID gameId) {
        List<CardStateDto> cardStates = cardStateService.getAllByGame(gameId);
        if (cardStates.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
        return ResponseEntity.ok(cardStates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardStateDto> getById(@PathVariable Long id) {
        CardStateDto cardState = cardStateService.getById(id);
        return ResponseEntity.ok(cardState);
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<CardStateDto>> getCardsByPlayer(@PathVariable Long playerId) {
        List<CardStateDto> cardStates = cardStateService.getCardsByPlayer(playerId);
        if (cardStates.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
        return ResponseEntity.ok(cardStates);
    }

    @PatchMapping("/trade/{playerId}")
    public ResponseEntity<String> validTrade(@PathVariable Long playerId) {
        PlayerDto playerDto = new PlayerDto();
        playerDto.setPlayerId(playerId);

        boolean isValidTrade = cardStateService.validTrade(playerDto);

        if (isValidTrade) {
            return ResponseEntity.ok("El intercambio es válido y puede ser realizado.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El intercambio no es válido. El jugador no tiene 3 cartas del mismo símbolo.");
        }
    }

    @PostMapping("/conquest")
    public ResponseEntity<CardStateDto> dealConquestCard(@RequestBody ConquestCardRequestDto requestDto) {
        CardStateDto cardStateDto = cardStateService.dealConquestCard(requestDto.getPlayer(), requestDto.getTurn());
        return ResponseEntity.ok(cardStateDto);
    }

    @PostMapping("/returnCard")
    public ResponseEntity<CardDto> returnCardToDeck(@RequestBody CardDto cardDto) {
        CardDto returnedCard = cardStateService.returnCardToDeck(cardDto);
        return ResponseEntity.ok(returnedCard);
    }
}
