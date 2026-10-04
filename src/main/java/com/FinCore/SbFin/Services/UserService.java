package com.FinCore.SbFin.Services;

import com.FinCore.SbFin.DTO.UserRequestDTO;
import com.FinCore.SbFin.DTO.UserResponseDTO;
import com.FinCore.SbFin.Entity.User;
import com.FinCore.SbFin.Exception.UserAlreadyExists;
import com.FinCore.SbFin.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO registerUser(UserRequestDTO dto) {

        Optional<User> userOptional = userRepository.findByEmail(dto.getEmail().toLowerCase());

        if(userOptional.isPresent()) {
            throw new UserAlreadyExists("User with email id:" + dto.getEmail() + " already exists");
        }

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword().toLowerCase());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setEmail(user.getEmail());
        responseDTO.setId(user.getId());
        responseDTO.setName(user.getName());

        return responseDTO;
    }
}
