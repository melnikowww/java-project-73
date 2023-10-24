package hexlet.code.controllers;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.querydsl.core.types.Predicate;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "${base.url}" + "/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final UserRepository userRepository;
    private static final String ONLY_OWNER_BY_ID = """
            @taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()
        """;

    @Operation(summary = "Get all tasks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of tasks")
    })
    @GetMapping(
        path = "",
        produces = "application/json"
    )
    public List<Task> getTasks(@QuerydslPredicate(root = Task.class) Predicate predicate) {
        return taskService.findWithFilter(predicate);
    }

    @Operation(summary = "Get task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get specific task by id")
    })
    @GetMapping(
        path = "/{id}",
        produces = "application/json"
    )
    public Task getTask(@PathVariable Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    @Operation(summary = "Create new task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task is successfully created"),
        @ApiResponse(responseCode = "422", description = "Data is not valid")
    })
    @PostMapping(
        path = "",
        produces = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(
        @Valid @RequestBody TaskDto dto) {
        return taskService.createTask(dto);
    }

    @Operation(summary = "Update task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task is successfully updated"),
        @ApiResponse(responseCode = "422", description = "Data is not valid")
    })
    @PutMapping(
        path = "/{id}",
        produces = "application/json"
    )
    public Task updateTask(
        @Valid @RequestBody TaskDto dto,
        @PathVariable Long id) {
        return taskService.updateTask(dto, id);
    }

    @Operation(summary = "Delete task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delete task by id")
    })
    @PreAuthorize(ONLY_OWNER_BY_ID)
    @DeleteMapping(
        path = "/{id}"
    )
    public void deleteTask(
        @PathVariable Long id) {
        taskRepository.deleteById(id);
    }
}
