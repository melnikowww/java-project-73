package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
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


import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestUtils utils;
    @Value(value = "${base.url}")
    private String prefix;
    private final String base = "http://localhost:8080";

    private String token;

    @BeforeEach
    void prepare() {
        utils.addUsers();
        token = utils.loginUser();
        utils.addTaskStatus("NEW_STAT1");
        utils.addTaskStatus("NEW_STAT2");
    }

    @AfterEach
    void clean() {
        utils.clean();
    }

    @Test
    public void testGetStatuses() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
            get(base + prefix + "/statuses")
        )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("NEW_STAT1");
        assertThat(response.getContentAsString()).contains("NEW_STAT2");

        final List<TaskStatus> taskStatuses = objectMapper
            .readValue(response.getContentAsString(), new TypeReference<>() { });
        assertThat(taskStatusRepository.findAll()).containsAll(taskStatuses);
    }

    @Test
    public void testGetStatus() throws Exception {
        TaskStatus taskStatus = taskStatusRepository.findByName("NEW_STAT1").orElseThrow();

        MockHttpServletResponse response = mockMvc.perform(
                get(base + prefix + "/statuses" + "/" + taskStatus.getId())
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("NEW_STAT1");

        final TaskStatus status = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() { });
        assertThat(status.getName())
            .isEqualTo(taskStatusRepository.findById(taskStatus.getId()).orElseThrow().getName());
    }

    @Test
    public void testCreateStatus() throws Exception {
        TaskStatusDto dto = new TaskStatusDto("TEST_STAT");

        MockHttpServletResponse response = mockMvc.perform(
                post(base + prefix + "/statuses")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto))
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getContentAsString()).contains(dto.getName());

        assertThat(taskStatusRepository.findByName(dto.getName()).orElseThrow()).isNotNull();
    }

    @Test
    public void testUpdateStatus() throws Exception {
        TaskStatusDto dto = new TaskStatusDto("TEST_STAT");

        TaskStatus taskStatus = taskStatusRepository.findByName("NEW_STAT1").orElseThrow();

        MockHttpServletResponse response = mockMvc.perform(
                put(base + prefix + "/statuses" + "/" + taskStatus.getId())
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto))
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(dto.getName());

        assertThat(taskStatusRepository.findByName(taskStatus.getName())).isEmpty();
        assertThat(taskStatusRepository.findByName(dto.getName())).isPresent();
    }

    @Test
    public void testDeleteStatus() throws Exception {
        TaskStatus taskStatus = taskStatusRepository.findByName("NEW_STAT1").orElseThrow();

        assertThat(taskStatus).isNotNull();
        assertThat(taskStatus.getName()).isEqualTo("NEW_STAT1");

        MockHttpServletResponse response = mockMvc.perform(
                delete(base + prefix + "/statuses" + "/" + taskStatus.getId())
                    .header("Authorization", "Bearer " + token)
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        Optional<TaskStatus> deletedTaskStatus = taskStatusRepository.findByName("NEW_STAT1");

        assertThat(deletedTaskStatus.isEmpty()).isTrue();
    }

}
