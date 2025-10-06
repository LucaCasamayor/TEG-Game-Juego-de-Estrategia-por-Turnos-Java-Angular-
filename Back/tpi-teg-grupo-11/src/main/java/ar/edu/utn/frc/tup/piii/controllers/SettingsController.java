package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.SettingsDto;
import ar.edu.utn.frc.tup.piii.services.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowCredentials = "true")
public class SettingsController {

    private final SettingsService settingsService;


    @GetMapping("/{id}")
    public ResponseEntity<SettingsDto> getSettingsById(@PathVariable Long id) {
        SettingsDto settingsFounded = settingsService.findById(id);
        if(Objects.isNull(settingsFounded)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(settingsFounded);
        }
    }

    @PostMapping
    public ResponseEntity<SettingsDto> saveSettings(@RequestBody SettingsDto settings) {
        SettingsDto result = settingsService.createSettings(settings);
        if(Objects.isNull(result)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(result);
        }
    }
}
