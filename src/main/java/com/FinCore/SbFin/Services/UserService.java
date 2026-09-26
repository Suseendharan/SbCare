package com.FinCore.SbFin.Services;

import com.FinCore.SbFin.Entity.User;
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

    public User registerUser(User user) {

        Optional<User> user1 = userRepository.findByEmail(user.getEmail());

        if(user1.isPresent()) {
            throw new RuntimeException("User with id " + user.getId() + " already exists");
        }

        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
}
