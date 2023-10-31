package hexlet.code.service;

import hexlet.code.dto.LogInDto;
import hexlet.code.config.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogInServiceImpl implements LogInService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public String authenticate(LogInDto logInDto) {
        var auth = new UsernamePasswordAuthenticationToken(
            logInDto.getEmail(), logInDto.getPassword());
        authenticationManager.authenticate(auth);
        return jwtUtils.generateToken(logInDto);
    }
}
