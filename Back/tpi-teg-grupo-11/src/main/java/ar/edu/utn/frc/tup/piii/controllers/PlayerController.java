package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.PlayerDto;
import ar.edu.utn.frc.tup.piii.services.interfaces.PlayerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(@Qualifier("humanPlayerServiceImpl") PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<List<PlayerDto>> getAll() {
        return ResponseEntity.ok(playerService.getAll());
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<PlayerDto>> getByGame(@PathVariable UUID gameId) {
        return ResponseEntity.ok(playerService.getPlayersByGame(gameId));
    }

    @PostMapping
    public ResponseEntity<PlayerDto> createPlayer(@RequestBody PlayerDto playerDto) {
        PlayerDto created = playerService.createPlayer(playerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
