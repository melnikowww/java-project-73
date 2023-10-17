package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskStatusRepository repository;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TestUtils utils;

//    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStatusController.class);

    private final String login = "http://localhost:8080/api/login";
    private final String base = "http://localhost:8080/api/statuses";
    private final String users = "http://localhost:8080/api/users";

    private String token;

    @BeforeEach
    void prepare() throws Exception {
        utils.addUsers();
        utils.loginUser();
        token = utils.token;
        utils.addTaskStatus("NEW_STAT1");
        utils.addTaskStatus("NEW_STAT2");
    }

    @Test
    public void testGetStatuses() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
            get(base)
        )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("NEW_STAT1");
        assertThat(response.getContentAsString()).contains("NEW_STAT2");
    }

    @Test
    public void testGetStatus() throws Exception {
        TaskStatus taskStatus = repository.findByName("NEW_STAT1").orElseThrow();

        assertThat(taskStatus).isNotNull();

        MockHttpServletResponse response = mockMvc.perform(
                get(base + "/" + taskStatus.getId())
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("NEW_STAT1");
    }

    @Test
    public void testCreateStatus() throws Exception {
        TaskStatusDto dto = new TaskStatusDto("TEST_STAT");

        MockHttpServletResponse response = mockMvc.perform(
                post(base)
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto))
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getContentAsString()).contains(dto.getName());
    }

    @Test
    public void testUpdateStatus() throws Exception {
        TaskStatusDto dto = new TaskStatusDto("TEST_STAT");

        TaskStatus taskStatus = repository.findByName("NEW_STAT1").orElseThrow();

        assertThat(taskStatus).isNotNull();
        assertThat(taskStatus.getName()).isEqualTo("NEW_STAT1");

        MockHttpServletResponse response = mockMvc.perform(
                put(base + "/" + taskStatus.getId())
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto))
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(dto.getName());
    }

    @Test
    public void testDeleteStatus() throws Exception {
        TaskStatus taskStatus = repository.findByName("NEW_STAT1").orElseThrow();

        assertThat(taskStatus).isNotNull();
        assertThat(taskStatus.getName()).isEqualTo("NEW_STAT1");

        MockHttpServletResponse response = mockMvc.perform(
                delete(base + "/" + taskStatus.getId())
                    .header("Authorization", "Bearer " + token)
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        Optional<TaskStatus> deletedTaskStatus = repository.findByName("NEW_STAT1");

        assertThat(deletedTaskStatus.isEmpty()).isTrue();
    }

}
