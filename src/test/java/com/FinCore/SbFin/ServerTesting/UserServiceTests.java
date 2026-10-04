package com.FinCore.SbFin.ServerTesting;

import com.FinCore.SbFin.DTO.UserRequestDTO;
import com.FinCore.SbFin.DTO.UserResponseDTO;
import com.FinCore.SbFin.Entity.User;
import com.FinCore.SbFin.Exception.UserAlreadyExists;
import com.FinCore.SbFin.Repository.UserRepository;
import com.FinCore.SbFin.Services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    public void SuccessRegisterUser() {

        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("sbcare@gmail.com");
        dto.setPassword("12345");
        dto.setName("sbcare");

        when(userRepository.findByEmail("sbcare@gmail.com")).thenReturn(Optional.empty());

        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserResponseDTO responseDTO = userService.registerUser(dto);

        assertEquals(dto.getName(), responseDTO.getName());
        assertEquals(dto.getEmail(), responseDTO.getEmail());

        verify(userRepository).save(any(User.class));

    }

    @Test
    public void RegisterUserFail() {

        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("sbcare@gmail.com");
        dto.setPassword("12345");
        dto.setName("sbcare");

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setCreatedAt(LocalDateTime.now());

        when(userRepository.findByEmail("sbcare@gmail.com")).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExists.class, () -> userService.registerUser(dto));

        verify(userRepository,never()).save(any(User.class));

    }
}
