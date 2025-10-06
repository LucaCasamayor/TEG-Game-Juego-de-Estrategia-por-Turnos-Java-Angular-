package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.PlayerDto;
import ar.edu.utn.frc.tup.piii.services.interfaces.PlayerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bot")
public class PlayerBotController {

    public PlayerBotController(@Qualifier("botPlayerServiceImpl") PlayerService service) {
        this.service = service;
    }

    @Qualifier("botPlayerServiceImpl")
    private final PlayerService service;

    @GetMapping
    public ResponseEntity<List<PlayerDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<PlayerDto>> getByGame(@PathVariable UUID gameId) {
        return ResponseEntity.ok(service.getPlayersByGame(gameId));
    }

    @PostMapping
    public ResponseEntity<PlayerDto> createPlayer(@RequestBody PlayerDto playerDto) {
        return ResponseEntity.ok(service.createPlayer(playerDto));
    }

}
