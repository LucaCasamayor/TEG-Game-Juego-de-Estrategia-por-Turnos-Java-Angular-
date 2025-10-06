package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.RecoveryDto;
import ar.edu.utn.frc.tup.piii.dtos.ResetPasswordDto;
import ar.edu.utn.frc.tup.piii.dtos.UserDto;
import ar.edu.utn.frc.tup.piii.entities.UserEntity;
import ar.edu.utn.frc.tup.piii.models.User;
import ar.edu.utn.frc.tup.piii.persistence.UserPersistence;
import ar.edu.utn.frc.tup.piii.repository.jpa.UserJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserPersistence persistence;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private UserService userService;

    @Test
    void loginUser() {
        String username = "testUser";
        String password = "pass";

        User user = new User();
        UserDto userDto = new UserDto();

        Mockito.when(persistence.findByUsernameAndPassword(username, password)).thenReturn(user);
        Mockito.when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto result = userService.loginUser(username, password);

        Assertions.assertEquals(userDto, result);
    }
    @Test
    void registerUser_shouldReturnNull_whenUserExists() {
        UserDto inputDto = new UserDto();
        inputDto.setUsername("exists");

        User existingUser = new User();

        Mockito.when(persistence.findByUsername(inputDto.getUsername())).thenReturn(existingUser);

        UserDto result = userService.registerUser(inputDto);

        Assertions.assertNull(result);
    }


    @Test
    void registerUser() {
        UserDto inputDto = new UserDto();
        inputDto.setUsername("newUser");

        User user = new User();
        User savedUser = new User();
        UserDto savedDto = new UserDto();

        Mockito.when(persistence.findByUsername(inputDto.getUsername())).thenReturn(null);
        Mockito.when(modelMapper.map(inputDto, User.class)).thenReturn(user);
        Mockito.when(persistence.save(user)).thenReturn(savedUser);
        Mockito.when(modelMapper.map(savedUser, UserDto.class)).thenReturn(savedDto);

        UserDto result = userService.registerUser(inputDto);

        Assertions.assertEquals(savedDto, result);
    }

    @Test
    void updateUser() {
        UUID userId = UUID.randomUUID();

        UserDto inputDto = new UserDto();
        inputDto.setUsername("newUsername");
        inputDto.setPassword("newPass");
        inputDto.setImgUrl("newImg");
        inputDto.setActive(true);

        User actualUser = new User();
        actualUser.setUsername("oldUsername");
        actualUser.setPassword("oldPass");
        actualUser.setImgUrl("oldImg");
        actualUser.setActive(false);

        User updatedUser = new User();
        UserDto returnedDto = new UserDto();

        Mockito.when(persistence.findById(userId)).thenReturn(actualUser);
        Mockito.when(persistence.findByUsername(inputDto.getUsername())).thenReturn(null);
        Mockito.when(persistence.update(userId, actualUser)).thenReturn(updatedUser);
        Mockito.when(modelMapper.map(updatedUser, UserDto.class)).thenReturn(returnedDto);

        UserDto result = userService.updateUser(userId, inputDto);

        Assertions.assertEquals(returnedDto, result);
        Assertions.assertEquals("newUsername", actualUser.getUsername());
        Assertions.assertEquals("newPass", actualUser.getPassword());
        Assertions.assertEquals("newImg", actualUser.getImgUrl());
        Assertions.assertTrue(actualUser.isActive());
    }

    @Test
    void sendCode() {
        String email = "test@example.com";
        String code = "123456";

        userService.sendCode(email, code);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(mailSender).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        Assertions.assertEquals(email, sentMessage.getTo()[0]);
        Assertions.assertEquals("Recuperación de contraseña", sentMessage.getSubject());
        Assertions.assertTrue(sentMessage.getText().contains(code));
    }

    @Test
    void sendRecoveryCode() {
        RecoveryDto request = new RecoveryDto();
        request.setUsername("user1");
        request.setEmail("email@test.com");

        UserEntity user = new UserEntity();

        Mockito.when(userRepository.findByUsername(request.getUsername())).thenReturn(user);

        userService.sendRecoveryCode(request);

        Mockito.verify(userRepository).save(Mockito.argThat(u -> u.getRecovery_code() != null));
        Mockito.verify(mailSender).send(Mockito.any(SimpleMailMessage.class));
    }
    @Test
    void sendRecoveryCode_shouldThrowNotFound_whenUserNull() {
        RecoveryDto request = new RecoveryDto();
        request.setUsername("noUser");

        Mockito.when(userRepository.findByUsername(request.getUsername())).thenReturn(null);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            userService.sendRecoveryCode(request);
        });
    }

    @Test
    void resetPassword() {
        ResetPasswordDto dto = new ResetPasswordDto();
        dto.setUsername("user1");
        dto.setCode("123456");
        dto.setNewPassword("newPass");

        UserEntity user = new UserEntity();
        user.setRecovery_code("123456");
        user.setRecovery_code_expiration(LocalDateTime.now().plusMinutes(5));

        Mockito.when(userRepository.findByUsername(dto.getUsername())).thenReturn(user);

        userService.resetPassword(dto);

        Assertions.assertEquals("newPass", user.getPassword());
        Assertions.assertNull(user.getRecovery_code());
        Assertions.assertNull(user.getRecovery_code_expiration());
        Mockito.verify(userRepository).save(user);
    }
    @Test
    void resetPassword_shouldThrowBadRequest_whenCodeInvalid() {
        ResetPasswordDto dto = new ResetPasswordDto();
        dto.setUsername("user1");
        dto.setCode("wrongCode");

        UserEntity user = new UserEntity();
        user.setRecovery_code("123456");

        Mockito.when(userRepository.findByUsername(dto.getUsername())).thenReturn(user);

        Assertions.assertThrows(ResponseStatusException.class, () -> userService.resetPassword(dto));
    }

    @Test
    void resetPassword_shouldThrowBadRequest_whenCodeExpired() {
        ResetPasswordDto dto = new ResetPasswordDto();
        dto.setUsername("user1");
        dto.setCode("123456");

        UserEntity user = new UserEntity();
        user.setRecovery_code("123456");
        user.setRecovery_code_expiration(LocalDateTime.now().minusMinutes(1));

        Mockito.when(userRepository.findByUsername(dto.getUsername())).thenReturn(user);

        Assertions.assertThrows(ResponseStatusException.class, () -> userService.resetPassword(dto));
    }
}