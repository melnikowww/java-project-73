package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.LabelDto;
import hexlet.code.repository.LabelRepository;
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


import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class LabelControllerTest {

    @Autowired
    LabelRepository labelRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TestUtils utils;
    String token;

    private final String base = "http://localhost:8080/api/labels";

    @BeforeEach
    public void prepare() {
        utils.addUser();
        utils.loginUser();
        token = utils.token;
        utils.addTaskStatus("NEW_STAT1");
        utils.addTask();
        utils.addLabels("TEST_LABEL");
        utils.addLabels("TEST_LABEL_1");
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
        assertThat(labelRepository.findByName("TEST_LABEL").isEmpty()).isTrue();
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

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(labelRepository.findByName("TEST_LABEL").isEmpty()).isFalse();
    }

}
