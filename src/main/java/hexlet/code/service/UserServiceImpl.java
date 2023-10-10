package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    UserRepository userRepository;
//    @Autowired
//    JWTUtils jwtUtils;

    @Override
    public User createUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setUserRole(userDto.getRole());
//        String jwtToken = jwtUtils.generateToken(user);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserDto userDto, Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setUserRole(userDto.getRole());
        return userRepository.save(user);
    }
}
