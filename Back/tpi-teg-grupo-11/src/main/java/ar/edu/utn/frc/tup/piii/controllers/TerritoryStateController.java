package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.TerritoryStateDto;
import ar.edu.utn.frc.tup.piii.services.TerritoryStateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/territoryState")
public class TerritoryStateController {

    private final TerritoryStateService service;

    public TerritoryStateController(TerritoryStateService service) {
        this.service = service;
    }

    @GetMapping("/player/{id}")
    public ResponseEntity<List<TerritoryStateDto>> getByPlayer(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByPlayer(id));
    }

//    @GetMapping("/turn/{id}")
//    public ResponseEntity<List<TerritoryStateDto>> getByTurn(@PathVariable Long id) {
//        return ResponseEntity.ok(service.findByTurn(id));
//    }

    @PostMapping
    public ResponseEntity<TerritoryStateDto> create(@RequestBody @Valid TerritoryStateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TerritoryStateDto> update(@PathVariable Long id, @RequestBody @Valid TerritoryStateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

}
