package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.GameMapDto;
import ar.edu.utn.frc.tup.piii.services.GameMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/maps")
@RequiredArgsConstructor
public class GameMapController {

    private final GameMapService gameMapService;

    @GetMapping
    public ResponseEntity<List<GameMapDto>> getAllMaps() {
        List<GameMapDto> maps = gameMapService.getAll();
        if (Objects.isNull(maps)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }
        else {
            return ResponseEntity.ok(maps);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameMapDto> getMapById(@PathVariable Long id) {
        GameMapDto mapa = gameMapService.getById(id);
        if (Objects.isNull(mapa)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }
        else {
            return ResponseEntity.ok(mapa);
        }
    }
}
