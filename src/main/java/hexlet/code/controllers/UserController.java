package hexlet.code.controllers;


import hexlet.code.UserRole;
import hexlet.code.dto.LogInDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.config.security.JwtUtils;
import hexlet.code.service.LogInService;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@Validated
@RestController()
@RequestMapping(path = "/api")
public class UserController {

    @Autowired
    hexlet.code.repository.UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    LogInService logInService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    TaskRepository taskRepository;

    @GetMapping(
        path = "/users/{id}",
        produces = "application/json")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(200).body(userRepository.findById(id).orElseThrow());
    }

    @GetMapping(
        path = "/users",
        produces = "application/json")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.status(200).body(userRepository.findAll());
    }

    @PostMapping(path = "/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) {
//        User user = new User();
//        user.setFirstName(userDto.getFirstName());
//        user.setLastName(userDto.getLastName());
//        user.setEmail(userDto.getEmail());
//        user.setPassword(userDto.getPassword());
        userDto.setRole(UserRole.USER);
//        user.setCreatedAt(userDto.getCreatedAt());
        return ResponseEntity.status(201).body(userService.createUser(userDto));
    }

    @PutMapping(path = "/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        return ResponseEntity.status(200).body(userService.updateUser(userDto, id));
    }

    @PostMapping("/login")
    public ResponseEntity<String> authUser(@Valid @RequestBody LogInDto logInDto) {
        return ResponseEntity.ok(logInService.authenticate(logInDto));
    }

    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        User user = userRepository.findUserById(id)
            .orElseThrow(() -> new UsernameNotFoundException("No one user was found!"));
        if (taskRepository.findByAuthor(user).isEmpty()) {
            userRepository.delete(user);
        }
    }
}
