package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.AICharacterDto;
import ar.edu.utn.frc.tup.piii.enums.AIProfile;
import ar.edu.utn.frc.tup.piii.services.AICharacterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ai-characters")
public class AICharacterController {

    private final AICharacterService aiCharacterService;

    public AICharacterController(AICharacterService aiCharacterService) {
        this.aiCharacterService = aiCharacterService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AICharacterDto>> getAll() {
        return ResponseEntity.ok(aiCharacterService.getAll());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<AICharacterDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(aiCharacterService.getById(id));
    }

    @GetMapping("/getByProfile/{profile}")
    public ResponseEntity<List<AICharacterDto>> getAllByProfile(@PathVariable AIProfile profile) {
        return ResponseEntity.ok(aiCharacterService.getAllByProfile(profile));
    }
}
