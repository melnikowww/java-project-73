package hexlet.code.controllers;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController()
@RequestMapping(path = "/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @GetMapping(
        path = "/{id}",
        produces = "application/json")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(200).body(userRepository.findById(id).orElseThrow());
    }

    @GetMapping(
        path = "",
        produces = "application/json")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.status(200).body(userRepository.findAll());
    }

    @PostMapping(path = "")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.status(201).body(userService.createUser(userDto));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        return ResponseEntity.status(200).body(userService.updateUser(userDto, id));
    }
}
