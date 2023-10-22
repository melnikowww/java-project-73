package hexlet.code.service;

import hexlet.code.dto.LogInDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.config.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogInServiceImpl implements LogInService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    @Override
    public String authenticate(LogInDto logInDto) {
        User realUser = repository.findUserByEmail(logInDto.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("No one user found with this email!"));

        String password = logInDto.getPassword();

        if (!encoder.matches(password, realUser.getPassword())) {
            throw new UsernameNotFoundException("Incorrect password");
        }
        return jwtUtils.generateToken(logInDto);
    }
}
