package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;

public interface UserService {
    User createUser(UserDto userDto);
    User updateUser(UserDto userDto, Long id);

    void deleteUser(Long id);
}
