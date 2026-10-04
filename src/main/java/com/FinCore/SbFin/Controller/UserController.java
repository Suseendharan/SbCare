    package com.FinCore.SbFin.Controller;

    import com.FinCore.SbFin.DTO.UserRequestDTO;
    import com.FinCore.SbFin.DTO.UserResponseDTO;
    import com.FinCore.SbFin.Entity.User;
    import com.FinCore.SbFin.Services.UserService;
    import jakarta.validation.Valid;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/user")
    public class UserController {

        private final UserService userService;

        public UserController(UserService userService) {
            this.userService = userService;
        }

        @PostMapping("/register")
        public ResponseEntity<UserResponseDTO> regsiterUser(@Valid @RequestBody UserRequestDTO user) {
            return new ResponseEntity<>(userService.registerUser(user), HttpStatus.OK);
        }
    }
