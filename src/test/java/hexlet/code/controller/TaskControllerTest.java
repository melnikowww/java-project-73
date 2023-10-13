package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.LogInDto;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskStatusRepository taskStatusRepository;
    @Autowired
    TaskRepository taskRepository;

    private String token;
    private final String login = "http://localhost:8080/api/login";
    private final String base = "http://localhost:8080/api/tasks";
    private final String users = "http://localhost:8080/api/users";

    @BeforeEach
    void prepare() throws Exception {
        UserDto userDto = new UserDto("senya@mail.ru", "Semyon", "Semyonich", "sem777");

        mockMvc.perform(post(users)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
            .andReturn();

        User user = userRepository.findUserByEmail("senya@mail.ru").orElseThrow();

        LogInDto logInDto = new LogInDto(userDto.getEmail(), userDto.getPassword());

        token = mockMvc.perform(post(login)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(logInDto))
            )
            .andReturn()
            .getResponse().getContentAsString();

        TaskStatus taskStatus1 = new TaskStatus("NEW_STAT1");
        taskStatusRepository.save(taskStatus1);

        Task task = new Task("TEST_TASK", "DESC", taskStatus1, user, user);
        taskRepository.save(task);
    }

    @Test
    public void getTasks() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(
                get(base)
                    .header("Authorization", "Bearer " + token)
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("TEST_TASK");
    }

    @Test
    public void getTask() throws Exception {
        Task task = taskRepository.findByName("TEST_TASK").orElseThrow();
        assertThat(task).isNotNull();

        MockHttpServletResponse response = mockMvc
            .perform(
                get(base + "/" + task.getId())
                    .header("Authorization", "Bearer " + token)
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("TEST_TASK");
    }

    @Test
    public void createTask() throws Exception {
        User user = userRepository.findUserByEmail("senya@mail.ru").orElseThrow();
        TaskStatus taskStatus = taskStatusRepository.findByName("NEW_STAT1").orElseThrow();

        TaskDto taskDto = new TaskDto(
            "NEW_TASK",
            "NEW_DESC",
            taskStatus.getId(),
            user.getId(),
            user.getId(),
            new HashSet<>()
        );

        MockHttpServletResponse response = mockMvc
            .perform(
                post(base)
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskDto))
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getContentAsString()).contains("NEW_TASK");
    }

    @Test
    public void updateTask() throws Exception {
        User user = userRepository.findUserByEmail("senya@mail.ru").orElseThrow();
        TaskStatus taskStatus = taskStatusRepository.findByName("NEW_STAT1").orElseThrow();

        TaskDto taskDto = new TaskDto(
            "NEW_TASK",
            "NEW_DESC",
            taskStatus.getId(),
            user.getId(),
            user.getId(),
            new HashSet<>()
        );

        Task oldTask = taskRepository.findByName("TEST_TASK").orElseThrow();
        assertThat(oldTask).isNotNull();

        MockHttpServletResponse response = mockMvc
            .perform(
                put(base + "/" + oldTask.getId())
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskDto))
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("NEW_TASK");
        assertThat(response.getContentAsString()).doesNotContain("TEST_TASK");
    }

    @Test
    public void deleteTask() throws Exception {
        Task task = taskRepository.findByName("TEST_TASK").orElseThrow();
        assertThat(task).isNotNull();

        MockHttpServletResponse response = mockMvc.perform(
            delete(base + "/" + task.getId())
                .header("Authorization", "Bearer " + token)
        )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(taskRepository.findByName("TEST_TASK").isEmpty()).isTrue();
    }
}
