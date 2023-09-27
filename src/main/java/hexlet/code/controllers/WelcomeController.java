package hexlet.code.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    @GetMapping("/welcome")
    public String root() {
        return "Welcome to Spring!";
    }

    @GetMapping("/")
    public String emptyRoot() {
        return "";
    }
}
