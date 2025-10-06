package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.enums.AIProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai-profile")
public class AiProfileController {
    @GetMapping
    public ResponseEntity<AIProfile[]> getAIProfiles() {
        return ResponseEntity.ok(AIProfile.values());
    }
}
