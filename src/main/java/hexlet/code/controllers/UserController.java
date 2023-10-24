package hexlet.code.controllers;

import hexlet.code.config.security.UserRole;
import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController()
@RequestMapping("${base.url}" + "/users")
@AllArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get specific user by id")
    })
    @GetMapping(
        path = "/{id}",
        produces = "application/json")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of all users")
    })
    @GetMapping(
        path = "",
        produces = "application/json")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User is successfully created"),
        @ApiResponse(responseCode = "422", description = "Data is not valid")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public User createUser(@Valid @RequestBody UserDto userDto) {
        if (userDto.getRole() == null) {
            userDto.setRole(UserRole.USER);
        }
        return userService.createUser(userDto);
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User is successfully updated"),
        @ApiResponse(responseCode = "422", description = "Data is not valid")
    })
    @PreAuthorize(ONLY_OWNER_BY_ID)
    @PutMapping(path = "/{id}")
    public User updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        return userService.updateUser(userDto, id);
    }

    @Operation(summary = "Delete user by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delete user by id")
    })
    @PreAuthorize(ONLY_OWNER_BY_ID)
    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
