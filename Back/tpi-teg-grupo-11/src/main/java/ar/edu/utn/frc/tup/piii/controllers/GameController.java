package ar.edu.utn.frc.tup.piii.controllers;


import ar.edu.utn.frc.tup.piii.dtos.GameDataDto;
import ar.edu.utn.frc.tup.piii.dtos.GameDto;
import ar.edu.utn.frc.tup.piii.dtos.JoinGameRequestDto;
import ar.edu.utn.frc.tup.piii.dtos.StatusDto;
import ar.edu.utn.frc.tup.piii.services.GamePersistenceService;
import ar.edu.utn.frc.tup.piii.services.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final GamePersistenceService gamePersistenceService;

    @GetMapping("/{gameId}")
    public ResponseEntity<GameDataDto> loadGameData(@PathVariable UUID gameId){

        GameDataDto game = gameService.findGameData(gameId);

        if(Objects.isNull(game)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una partida con ese código");
        } else {
            return ResponseEntity.ok(game);
        }
    }

    @GetMapping("/lobby/{gameId}")
    public ResponseEntity<GameDto> loadGame(@PathVariable UUID gameId){

        GameDto game = gameService.findGame(gameId);

        if(Objects.isNull(game)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una partida con ese código");
        } else {
            return ResponseEntity.ok(game);
        }
    }

    @PostMapping
    public ResponseEntity<GameDto> createGame(@Valid @RequestBody GameDto game) {
        GameDto gameSaved = gameService.createGame(game);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(gameSaved.getGameId())
                .toUri();

        return ResponseEntity.created(location).body(gameSaved);
    }


    @PutMapping("/state/{gameId}")
    public ResponseEntity<GameDto> updateGameState(@PathVariable UUID gameId, @RequestBody StatusDto state){
        GameDto gameUpdated = gameService.updateGameState(gameId, state.getGameState());
        if(Objects.isNull(gameUpdated)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo actualizar la partida");
        } else {
            return ResponseEntity.ok(gameUpdated);
        }
    }

    @PutMapping("/{gameId}")
    public ResponseEntity<GameDto> updateGame(@PathVariable UUID gameId, @RequestBody GameDto gameDto){
        GameDto gameUpdated = gamePersistenceService.updateGame(gameId, gameDto);
        if(Objects.isNull(gameUpdated)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo actualizar la partida");
        } else {
            return ResponseEntity.ok(gameUpdated);
        }
    }

    @PutMapping("/in-game/{gameId}")
    public ResponseEntity<GameDataDto> updateGameInGame(@PathVariable UUID gameId, @RequestBody GameDataDto gameDataDto) throws Exception {
        GameDataDto result = gameService.updateGameInGame(gameId, gameDataDto);
        if(Objects.isNull(result)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo actualizar la partida");
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @PutMapping("/in-game-phase/{gameId}")
    public ResponseEntity<GameDataDto> updateGamePhase(@PathVariable UUID gameId, @RequestBody GameDataDto gameDataDto) throws Exception {
        GameDataDto result = gameService.nextPhase(gameId, gameDataDto);
        if(Objects.isNull(result)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo actualizar la partida");
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @PutMapping("/join/{gameId}")
    public ResponseEntity<GameDto> joinGame(
            @PathVariable UUID gameId,
            @RequestBody JoinGameRequestDto game
            ) {
        GameDto result = gameService.joinGame(gameId, game.getUserId(), game.getPassword());
        return ResponseEntity.ok(result);
    }

}
