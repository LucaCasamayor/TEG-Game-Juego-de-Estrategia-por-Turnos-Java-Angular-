package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.TurnDto;
import ar.edu.utn.frc.tup.piii.models.Turn;
import ar.edu.utn.frc.tup.piii.services.GameService;
import ar.edu.utn.frc.tup.piii.services.implementations.TurnServiceCommon;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/turnos")
@RequiredArgsConstructor
public class TurnController {

    //    private final TurnService turnService;
    private final ModelMapper modelMapper;

    //    @Qualifier("turnHumanImpl")
    private final TurnServiceCommon turnService;
    private GameService gameService;
    //Post para cambiar de fase
    //Get para consultar el estado actual

//    @GetMapping("/games/{gameId}/current-turn")
//    public ResponseEntity<Turn> getCurrentTurn(@PathVariable UUID gameId) {
//        Turn currentTurn = turnService.getCurrentTurn(gameId);
//        return ResponseEntity.ok(currentTurn);
//    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<TurnDto>> getTurnsByGame(@PathVariable String gameId) {
        List<Turn> turns = turnService.getTurnsByGame(gameId);
        List<TurnDto> turnDtos = turns.stream()
                .map(turn -> modelMapper.map(turn, TurnDto.class))
                .toList();
        return ResponseEntity.ok(turnDtos);
    }

    @GetMapping("/{turnId}/player/{playerId}/did-conquer")
    public ResponseEntity<Boolean> didPlayerConquer(@PathVariable Long turnId, @PathVariable Long playerId) {
        boolean conquered = turnService.didPlayerConquerThisTurn(playerId, turnId);
        return ResponseEntity.ok(conquered);
    }
}

//    @PostMapping
//    public ResponseEntity<TurnDto> createTurn(TurnDto turn) {
//        Turn result = turnService.playTurn(modelMapper.map(turn, Turn.class));
//        return ResponseEntity.ok(modelMapper.map(result, TurnDto.class));
//    }