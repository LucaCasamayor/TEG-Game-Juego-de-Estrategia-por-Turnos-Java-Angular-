package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.*;
import ar.edu.utn.frc.tup.piii.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService IUserService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto userDto;
    private LoginRequestDto loginDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setUserId(UUID.randomUUID());
        userDto.setUsername("testuser");
        userDto.setPassword("testpass");
        userDto.setActive(true);
        userDto.setCreationDate(new Date());
        userDto.setImgUrl("http://img.url");

        loginDto = new LoginRequestDto("testuser", "testpass");
    }

    @Test
    void login_shouldReturnOk() throws Exception {
        Mockito.when(IUserService.loginUser(eq("testuser"), eq("testpass"))).thenReturn(userDto);

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void login_shouldReturnNotFound() throws Exception {
        Mockito.when(IUserService.loginUser(eq("testuser"), eq("testpass"))).thenReturn(null);

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void register_shouldReturnOk() throws Exception {
        Mockito.when(IUserService.registerUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void register_shouldReturnBadRequest() throws Exception {
        Mockito.when(IUserService.registerUser(any(UserDto.class))).thenReturn(null);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldReturnOk() throws Exception {
        UUID userId = UUID.randomUUID();
        Mockito.when(IUserService.updateUser(eq(userId), any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(put("/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void update_shouldReturnBadRequest() throws Exception {
        UUID userId = UUID.randomUUID();
        Mockito.when(IUserService.updateUser(eq(userId), any(UserDto.class))).thenReturn(null);

        mockMvc.perform(put("/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendRecoveryCode_shouldReturnOk() throws Exception {
        RecoveryDto recoveryDto = new RecoveryDto("testuser", "test@example.com");
        Mockito.doNothing().when(IUserService).sendRecoveryCode(any(RecoveryDto.class));

        mockMvc.perform(post("/user/recover-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recoveryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Código enviado al correo."));
    }

    @Test
    void resetPassword_shouldReturnOk() throws Exception {
        ResetPasswordDto resetDto = new ResetPasswordDto("testuser", "123456", "newPassword123");
        Mockito.doNothing().when(IUserService).resetPassword(any(ResetPasswordDto.class));

        mockMvc.perform(post("/user/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Contraseña actualizada correctamente."));
    }
}
