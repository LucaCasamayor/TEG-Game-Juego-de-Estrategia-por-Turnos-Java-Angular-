package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.MovementDto;
import ar.edu.utn.frc.tup.piii.services.MovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/movements")
@RequiredArgsConstructor
public class MovementController {

    private final MovementService service;

//    @GetMapping("/all/{gameId}")
//    public ResponseEntity<List<MovementDto>> getAllByGame(@PathVariable UUID gameId){
//        List<MovementDto> movements = service.getAllByGame(gameId);
//        return ResponseEntity.ok(movements);
//    }

    @GetMapping("/{movementId}")
    public ResponseEntity<MovementDto> getMovementById(@PathVariable Long movementId){
        MovementDto movement = service.getMovementById(movementId);

        if(Objects.isNull(movement)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find movement");
        } else {
            return ResponseEntity.ok(movement);
        }
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<MovementDto>> getAllMovementsByGame(@PathVariable String gameId) {
        return ResponseEntity.ok(service.getAllByGame(gameId));
    }



    @PostMapping
    public ResponseEntity<MovementDto> saveMovement(@RequestBody MovementDto movement){
        MovementDto saved = service.saveMovement(movement);

        if(Objects.isNull(saved)){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't save movement");
        } else {
            return ResponseEntity.ok(saved);
        }
    }



}
