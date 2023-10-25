package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TestUtils utils;

    private String token;
    @Value(value = "${base.url}")
    private String prefix;
    private final String base = "http://localhost:8080";


    @BeforeEach
    void prepare() {
        utils.addUsers();
        token = utils.loginUser();
        utils.addTaskStatus("NEW_STAT1");
        utils.addTask();
    }

    @AfterEach
    void clean() {
        utils.clean();
    }

    @Test
    public void getTasksTest() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(
                get(base + prefix + "/tasks")
                    .header("Authorization", "Bearer " + token)
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        final List<Task> tasks = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Task>>() {
        });
        final List<Task> expected = taskRepository.findAll();
        assertThat(tasks).containsAll(expected);
    }

    @Test
    public void getTasksWithParamsTest() throws Exception {
        User user = userRepository.findUserByEmail("senya@mail.ru").orElseThrow();

        MockHttpServletResponse response = mockMvc
            .perform(
                get(base + prefix + "/tasks" + "?authorId=" + user.getId())
                    .header("Authorization", "Bearer " + token)
            )
            .andReturn().getResponse();

        final List<Task> expected = taskRepository.findAll().stream()
            .filter(task -> task.getAuthor().getId().equals(user.getId()))
            .collect(Collectors.toList());
        final List<Task> actual = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual).isEqualTo(expected);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("TEST_TASK");
    }

    @Test
    public void getTaskTest() throws Exception {
        Task task = taskRepository.findByName("TEST_TASK").orElseThrow();

        MockHttpServletResponse response = mockMvc
            .perform(
                get(base + prefix + "/tasks" + "/" + task.getId())
                    .header("Authorization", "Bearer " + token)
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("TEST_TASK");
        final Task readTask = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() { });
        assertThat(readTask.getName()).isEqualTo(taskRepository.findById(task.getId()).orElseThrow().getName());
    }

    @Test
    @Transactional
    public void createTaskTest() throws Exception {
        User user = userRepository.findUserByEmail("senya@mail.ru").orElseThrow();
        TaskStatus taskStatus = taskStatusRepository.findByName("NEW_STAT1").orElseThrow();

        TaskDto taskDto = new TaskDto(
            "NEW_TASK",
            "NEW_DESC",
            user.getId(),
            taskStatus.getId(),
            new HashSet<>()
        );

        MockHttpServletResponse response = mockMvc
            .perform(
                post(base + prefix + "/tasks")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskDto))
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getContentAsString()).contains("NEW_TASK");
        assertThat(taskRepository.findByName(taskDto.getName())).isNotNull();
    }

    @Test
    @Transactional
    public void updateTaskTest() throws Exception {
        User user = userRepository.findUserByEmail("senya@mail.ru").orElseThrow();
        TaskStatus taskStatus = taskStatusRepository.findByName("NEW_STAT1").orElseThrow();
        TaskDto taskDto = new TaskDto(
            "NEW_TASK",
            "NEW_DESC",
            user.getId(),
            taskStatus.getId(),
            new HashSet<>()
        );

        Task oldTask = taskRepository.findByName("TEST_TASK").orElseThrow();

        MockHttpServletResponse response = mockMvc
            .perform(
                put(base + prefix + "/tasks" + "/" + oldTask.getId())
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskDto))
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("NEW_TASK");
        assertThat(response.getContentAsString()).doesNotContain("TEST_TASK");
        assertThat(taskRepository.findById(oldTask.getId()).orElseThrow().getName()).isEqualTo("NEW_TASK");
    }

    @Test
    @Transactional
    public void deleteTaskTest() throws Exception {
        Task task = taskRepository.findByName("TEST_TASK").orElseThrow();

        MockHttpServletResponse response = mockMvc.perform(
            delete(base + prefix + "/tasks" + "/" + task.getId())
                .header("Authorization", "Bearer " + token)
        )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(taskRepository.findById(task.getId()).isEmpty()).isTrue();
    }
}
