package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@SpringBootTest
@AutoConfigureMockMvc
public class LabelControllerTest {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestUtils utils;
    private String token;

    private final String base = "http://localhost:8080/api/labels";

    @BeforeEach
    public void prepare() {
        utils.addUser();
        token = utils.loginUser();
        utils.addTaskStatus("NEW_STAT1");
        utils.addTask();
        utils.addLabels("TEST_LABEL");
        utils.addLabels("TEST_LABEL_1");
    }

    @AfterEach
    void clean() {
        utils.clean();
    }

    @Test
    public void getLabelsTest() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(
                get(base)
                    .header("Authorization", "Bearer " + token)
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("TEST_LABEL");
        assertThat(response.getContentAsString()).contains("TEST_LABEL_1");

        final List<Label> labelList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() { });
        for (Label label: labelList) {
            assertThat(labelRepository.findAll().contains(label));
        }
    }

    @Test
    public void getLabelTest() throws Exception {
        Long labelId = labelRepository.findByName("TEST_LABEL").orElseThrow().getId();

        MockHttpServletResponse response = mockMvc
            .perform(
                get(base + "/" + labelId)
                    .header("Authorization", "Bearer " + token)
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("TEST_LABEL");

        final Label label = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() { });
        assertThat(label.getName())
            .isEqualTo(labelRepository.findById(labelId).orElseThrow().getName());
    }

    @Test
    public void createLabelTest() throws Exception {
        LabelDto dto = new LabelDto("NEW_LABEL");

        MockHttpServletResponse response = mockMvc
            .perform(
                post(base)
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto))
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getContentAsString()).contains("NEW_LABEL");
        assertThat(labelRepository.findByName("NEW_LABEL").isPresent()).isTrue();
    }

    @Test
    public void updateLabelTest() throws Exception {
        LabelDto dto = new LabelDto("NEW_LABEL");
        Long idToUpd = labelRepository.findByName("TEST_LABEL").orElseThrow().getId();

        MockHttpServletResponse response = mockMvc
            .perform(
                put(base + "/" + idToUpd)
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto))
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("NEW_LABEL");
        assertThat(labelRepository.findByName("TEST_LABEL")).isEmpty();
        assertThat(labelRepository.findByName(dto.getName())).isPresent();
    }

    @Test
    public void deleteLabelTest() throws Exception {
        Long id = labelRepository.findByName("TEST_LABEL").orElseThrow().getId();

        MockHttpServletResponse response = mockMvc
            .perform(
                delete(base + "/" + id)
                    .header("Authorization", "Bearer " + token)
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(labelRepository.findByName("TEST_LABEL").isEmpty()).isTrue();
    }

    @Test
    public void deleteLabelRefByTaskTest() throws Exception {
        utils.addTaskWithLabel();

        Long id = labelRepository.findByName("TEST_LABEL").orElseThrow().getId();

        MockHttpServletResponse response = mockMvc
            .perform(
                delete(base + "/" + id)
                    .header("Authorization", "Bearer " + token)
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(labelRepository.findByName("TEST_LABEL").isEmpty()).isFalse();
    }

}
