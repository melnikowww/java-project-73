package hexlet.code.controllers;

import hexlet.code.dto.LogInDto;
import hexlet.code.service.LogInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("${base.url}")
public class LoginController {

    @Autowired
    LogInService logInService;

    @Operation(summary = "Log in user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Log in user")
    })
    @PostMapping("/login")
    public String authUser(@Valid @RequestBody LogInDto logInDto) {
        return logInService.authenticate(logInDto);
    }

}
