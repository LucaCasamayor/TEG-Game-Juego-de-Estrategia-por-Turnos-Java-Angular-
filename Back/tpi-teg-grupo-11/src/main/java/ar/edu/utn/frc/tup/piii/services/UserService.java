package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.ResetPasswordDto;
import ar.edu.utn.frc.tup.piii.dtos.UserDto;
import ar.edu.utn.frc.tup.piii.dtos.RecoveryDto;
import ar.edu.utn.frc.tup.piii.entities.UserEntity;
import ar.edu.utn.frc.tup.piii.models.User;
import ar.edu.utn.frc.tup.piii.persistence.UserPersistence;
import ar.edu.utn.frc.tup.piii.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService{

    private final UserPersistence persistence;
    private final ModelMapper modelMapper;
    private final UserJpaRepository userRepository;
    private final JavaMailSender mailSender;


    public UserDto loginUser(String username, String password) {
        User result = persistence.findByUsernameAndPassword(username, password);
        return modelMapper.map(result, UserDto.class);
    }

    public UserDto registerUser(UserDto user) {
        User exists = persistence.findByUsername(user.getUsername());
        if(exists != null) {
            return null;
        } else {
            User result = persistence.save(modelMapper.map(user, User.class));
            return modelMapper.map(result, UserDto.class);
        }
    }

    public UserDto updateUser(UUID id, UserDto user) {
        User actualUser = persistence.findById(id);

        if(!actualUser.getUsername().equals(user.getUsername())
            && Objects.isNull(persistence.findByUsername(user.getUsername()))) {
            actualUser.setUsername(user.getUsername());
        }

        actualUser.setPassword(user.getPassword());
        actualUser.setImgUrl(user.getImgUrl());
        actualUser.setActive(user.isActive());

        User result = persistence.update(id, actualUser);

        return modelMapper.map(result, UserDto.class);
    }

    public void sendCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperación de contraseña");
        message.setText("Tu código de recuperación es: " + code);
        mailSender.send(message);
    }

    public void sendRecoveryCode(RecoveryDto request) {
        UserEntity user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.");
        }

        String code = String.format("%06d", new Random().nextInt(999999));
        user.setRecovery_code(code);
        user.setRecovery_code_expiration(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        sendCode(request.getEmail(), code);
    }

    public void resetPassword(ResetPasswordDto dto) {
        UserEntity user = userRepository.findByUsername(dto.getUsername());
        if (user == null || !dto.getCode().equals(user.getRecovery_code())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código inválido.");
        }

        if (user.getRecovery_code_expiration().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código expirado.");
        }

        user.setPassword(dto.getNewPassword());
        user.setRecovery_code(null);
        user.setRecovery_code_expiration(null);
        userRepository.save(user);
    }

}
