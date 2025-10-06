package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.LoginRequestDto;
import ar.edu.utn.frc.tup.piii.dtos.RecoveryDto;
import ar.edu.utn.frc.tup.piii.dtos.ResetPasswordDto;
import ar.edu.utn.frc.tup.piii.dtos.UserDto;
import ar.edu.utn.frc.tup.piii.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowCredentials = "true")
@RequiredArgsConstructor
public class UserController {

    private final UserService IUserService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto login) {

        UserDto user = IUserService.loginUser(login.getUsername(), login.getPassword());

        if(Objects.isNull(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username or email already exists");
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid UserDto user) {
        UserDto userSaved = IUserService.registerUser(user);
        if(Objects.isNull(userSaved)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User or email already exists.");
        } else {
            return ResponseEntity.ok(userSaved);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable UUID id, @RequestBody @Valid UserDto user) {
        UserDto userSaved = IUserService.updateUser(id, user);
        if(Objects.isNull(userSaved)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User or email already exists.");
        } else {
            return ResponseEntity.ok(userSaved);
        }
    }

    @PostMapping("/recover-password")
    public ResponseEntity<?> sendRecoveryCode(@RequestBody RecoveryDto request) {
        IUserService.sendRecoveryCode(request);
        return ResponseEntity.ok(Map.of("message", "Código enviado al correo."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto dto) {
        IUserService.resetPassword(dto);
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente."));
    }
}
