package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import hexlet.code.controllers.UserController;
import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
//@Transactional
//@DBRider
@DataSet("users.yml")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private String baseUrl = "http://localhost:8080";

    @BeforeEach
    void addUsers() throws Exception {
        UserDto userDto1 = new UserDto("petr@ya.ru", "Petr", "Petrovich", "petrusha13");
        UserDto userDto2 = new UserDto("senya@mail.ru", "Semyon", "Semyonich", "sem777");

        mockMvc.perform(post(baseUrl + "/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto1)))
            .andReturn();

        mockMvc.perform(post(baseUrl + "/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto2)))
            .andReturn();
    }

    @Test
    public void getUsersTest() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(get(baseUrl + "/api/users"))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThatJson(response.getContentAsString()).isArray();
        assertThat(response.getContentAsString()).contains("Petr");
        assertThat(response.getContentAsString()).contains("Semyon");
    }

    @Test
    public void getUserByIdTest() throws Exception {
        Long id = 2L;
        MockHttpServletResponse response = mockMvc
            .perform(get(baseUrl + "/api/users/" + id))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("senya@mail.ru");
    }

    @Test
    public void createUserTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Ivan");
        userDto.setLastName("Ivanov");
        userDto.setEmail("ivan@mail.ru");
        userDto.setPassword("12345");

        String content = objectMapper.writeValueAsString(userDto);

        MockHttpServletResponse response = mockMvc
            .perform(post(baseUrl + "/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(201);

        User user = userRepository.findUserByEmail("ivan@mail.ru");
        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo("Ivan");
        assertThat(user.getPassword()).isNotEqualTo("12345");
    }

    @Test
    public void updateUserTest() throws Exception {
        User user = userRepository.findUserByEmail("senya@mail.ru");
        UserDto userDto = new UserDto(user.getEmail(), "Senya", user.getLastName(), "666777");

        MockHttpServletResponse response = mockMvc
            .perform(put(baseUrl + "/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        user = userRepository.findUserByEmail("senya@mail.ru");
        assertThat(user.getFirstName()).isEqualTo("Senya");
    }

}


