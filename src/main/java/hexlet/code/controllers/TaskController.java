package hexlet.code.controllers;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import com.querydsl.core.types.Predicate;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "${base.url}")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskService taskService;

    @Operation(summary = "Get all tasks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of tasks")
    })
    @GetMapping(
        path = "/tasks",
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
        path = "/tasks/{id}",
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
        path = "/tasks",
        produces = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(
        @Valid @RequestBody TaskDto dto,
        @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        dto.setAuthorId(taskService.findAuthorId(token));
        return taskService.createTask(dto);
    }

    @Operation(summary = "Update task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task is successfully updated"),
        @ApiResponse(responseCode = "422", description = "Data is not valid")
    })
    @PutMapping(
        path = "/tasks/{id}",
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
    @DeleteMapping(
        path = "/tasks/{id}"
    )
    public void deleteTask(
        @PathVariable Long id,
        @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        taskService.deleteTask(id, token);
    }
}