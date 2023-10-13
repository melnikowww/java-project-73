package hexlet.code.utils;

import hexlet.code.UserRole;
import hexlet.code.dto.LabelDto;
import hexlet.code.dto.LogInDto;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TestUtils {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskStatusRepository taskStatusRepository;
    @Autowired
    TaskRepository taskRepository;
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
    private final String email = "senya@mail.ru";
    private final String password = "sem777";

    public void addUser() throws Exception {
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

    public void addTaskStatus() {
        TaskStatusDto dto = new TaskStatusDto("NEW_STAT1");
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
            new HashSet<>()
        );
        taskService.createTask(dto);
    }

    public void addLabels() {
        LabelDto dto = new LabelDto("TEST_LABEL");
        labelService.createLabel(dto);
        LabelDto dto1 = new LabelDto("TEST_LABEL_1");
        labelService.createLabel(dto1);
    }

}
