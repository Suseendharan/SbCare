package com.FinCore.SbFin.IntegrationTesting;

import com.FinCore.SbFin.Entity.User;
import com.FinCore.SbFin.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.web.servlet.function.ServerResponse.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerUser() throws Exception {
        mockMvc.perform(post("/user/register").content("""
                {
                    "name": "John",
                    "email": "john@gmail.com",
                    "password": "12345"
                }
                """).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@gmail.com"));

        Optional<User> user = userRepository.findByEmail("john@gmail.com");

        assertTrue(user.isPresent());

    }

    @Test
    void registerUserEmailAlreadyExists() throws Exception {
        mockMvc.perform(post("/user/register").content("""
                {
                    "name": "John",
                    "email": "john@gmail.com",
                    "password": "12345"
                }
                """).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
//                .andExpect(jsonPath("$.name").value("John"))
//                .andExpect(jsonPath("$.email").value("john@gmail.com"));

        Optional<User> user = userRepository.findByEmail("john@gmail.com");

        assertTrue(user.isPresent());

    }
}
