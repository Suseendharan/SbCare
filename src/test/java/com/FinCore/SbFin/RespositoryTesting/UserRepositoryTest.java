package com.FinCore.SbFin.RespositoryTesting;

import com.FinCore.SbFin.Entity.User;
import com.FinCore.SbFin.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void findByEmailId() {
        User user = new User();
        user.setName("John");
        user.setEmail("sb@gmail.com");
        user.setPassword("12345");

        userRepository.save(user);

        Optional<User> opUser = userRepository.findByEmail("sb@gmail.com");

        assertTrue(opUser.isPresent());
        assertEquals(opUser.get().getName(), user.getName());
    }

    @Test
    public void findByEmailIdNotFound() {

        Optional<User> opUser = userRepository.findByEmail("sb@gmail.com");

        assertTrue(opUser.isEmpty());
    }
}
