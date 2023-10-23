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
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.LabelService;
import hexlet.code.service.LogInService;
import hexlet.code.service.TaskService;
import hexlet.code.service.TaskStatusService;
import hexlet.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
public class TestUtils {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private LogInService logInService;
    @Autowired
    private TaskStatusService taskStatusService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private LabelService labelService;

    private final String email = "senya@mail.ru";
    private final String password = "sem777";

    public void addUser() {
        UserDto userDto = new UserDto("senya@mail.ru", "Semyon", "Semyonich", "sem777");
        userDto.setRole(UserRole.USER);
        if (userRepository.findUserByEmail("senya@mail.ru").isEmpty()) {
            userService.createUser(userDto);
        }
    }

    public void addUsers() {
        UserDto userDto = new UserDto("senya@mail.ru", "Semyon", "Semyonich", "sem777");
        userDto.setRole(UserRole.USER);
        userService.createUser(userDto);

        UserDto userDto1 = new UserDto("petr@ya.ru", "Petr", "Petrovich", "petrusha13");
        userDto.setRole(UserRole.USER);
        userService.createUser(userDto1);
    }

    public String loginUser() {
        LogInDto logInDto = new LogInDto(email, password);
        return logInService.authenticate(logInDto);
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
            user.getId(),
            status.getId(),
            new HashSet<>()
        );
        taskService.createTask(dto, userRepository.findUserByEmail("senya@mail.ru").orElseThrow().getId());
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
            Set.of(labelRepository.findByName("TEST_LABEL").orElseThrow().getId())
        );
        taskService.createTask(dto, userRepository.findUserByEmail("senya@mail.ru").orElseThrow().getId());
    }

    public void clean() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();
    }

}
