package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder encoder;
    private final hexlet.code.repository.UserRepository userRepository;

    @Override
    public User createUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setUserRole(userDto.getRole());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserDto userDto, Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setUserRole(user.getUserRole());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getCurrentUser() {
        return userRepository
            .findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
            .orElseThrow();
    }
}
