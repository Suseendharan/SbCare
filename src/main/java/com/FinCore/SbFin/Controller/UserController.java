    package com.FinCore.SbFin.Controller;

    import com.FinCore.SbFin.Entity.User;
    import com.FinCore.SbFin.Services.UserService;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/user")
    public class UserController {

        private final UserService userService;

        public UserController(UserService userService) {
            this.userService = userService;
        }

        @PostMapping("/register")
        public User regsiterUser(@RequestBody User user) {
            System.out.println(user);
            User newUser = userService.registerUser(user);

            return newUser;
        }
    }
