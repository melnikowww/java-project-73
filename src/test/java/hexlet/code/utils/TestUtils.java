package hexlet.code.utils;

import hexlet.code.UserRole;
import hexlet.code.dto.LabelDto;
import hexlet.code.dto.LogInDto;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.LabelService;
import hexlet.code.service.LogInService;
import hexlet.code.service.TaskService;
import hexlet.code.service.TaskStatusService;
import hexlet.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


@Component
public class TestUtils {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskStatusRepository taskStatusRepository;
    @Autowired
    LabelRepository labelRepository;
    @Autowired
    UserService userService;
    @Autowired
    LogInService logInService;
    @Autowired
    TaskStatusService taskStatusService;
    @Autowired
    TaskService taskService;
    @Autowired
    LabelService labelService;

    public String token;
    public final String email = "senya@mail.ru";
    private final String password = "sem777";

    public void setProfile() {

    }

    public void addUser() {
        UserDto userDto = new UserDto("senya@mail.ru", "Semyon", "Semyonich", "sem777");
        userDto.setRole(UserRole.USER);
        userService.createUser(userDto);
    }

    public void addUsers() {
        UserDto userDto = new UserDto("senya@mail.ru", "Semyon", "Semyonich", "sem777");
        userDto.setRole(UserRole.USER);
        userService.createUser(userDto);

        UserDto userDto1 = new UserDto("petr@ya.ru", "Petr", "Petrovich", "petrusha13");
        userDto.setRole(UserRole.USER);
        userService.createUser(userDto1);
    }

    public void loginUser() {
        LogInDto logInDto = new LogInDto(email, password);
        token = logInService.authenticate(logInDto);
    }

    public void addTaskStatus(String name) {
        TaskStatusDto dto = new TaskStatusDto(name);
        taskStatusService.createStatus(dto);
    }

    public void addTask() {
        User user = userRepository.findUserByEmail(email).orElseThrow();
        TaskStatus status = taskStatusRepository.findByName("NEW_STAT1").orElseThrow();
        TaskDto dto = new TaskDto(
            "TEST_TASK",
            "TEST_DESC",
            status.getId(),
            user.getId(),
            user.getId(),
            new ArrayList<>()
        );
        taskService.createTask(dto);
    }

    public void addLabels(String name) {
        LabelDto dto = new LabelDto(name);
        labelService.createLabel(dto);
    }

    public void addTaskWithLabel() {
        User user = userRepository.findUserByEmail(email).orElseThrow();
        TaskStatus status = taskStatusRepository.findByName("NEW_STAT1").orElseThrow();
        TaskDto dto = new TaskDto(
            "TEST_TASK_WITH_LABEL",
            "TEST_DESC",
            status.getId(),
            user.getId(),
            user.getId(),
            List.of(labelRepository.findByName("TEST_LABEL").orElseThrow().getId())
        );
        taskService.createTask(dto);
    }

}
