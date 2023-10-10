package hexlet.code.service;

import hexlet.code.dto.LogInDto;

public interface LogInService {
    String authenticate(LogInDto logInDto);
}
