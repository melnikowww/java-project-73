package hexlet.code.controllers;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${base.url}")
@Validated
public class TaskStatusController {

    @Autowired
    TaskStatusRepository taskStatusRepository;
    @Autowired
    TaskStatusService taskStatusService;
    @Autowired
    TaskRepository taskRepository;

    @Operation(summary = "Get all task statuses")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of task statuses")
    })
    @GetMapping(
        path = "/statuses",
        produces = "application/json"
    )
    public List<TaskStatus> getStatuses() {
        return taskStatusRepository.findAll();
    }

    @Operation(summary = "Get task status by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get specific task status by id")
    })
    @GetMapping(
        path = "/statuses/{id}",
        produces = "application/json"
    )
    public TaskStatus getTaskStatus(@Valid @PathVariable Long id) {
        return taskStatusRepository.findById(id).orElseThrow();
    }

    @Operation(summary = "Create new task status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task status is successfully created"),
        @ApiResponse(responseCode = "422", description = "Data is not valid")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
        path = "/statuses",
        produces = "application/json"
    )
    public TaskStatus createStatus(@Valid @RequestBody TaskStatusDto dto) {
        return taskStatusService.createStatus(dto);
    }

    @Operation(summary = "Update task status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task status is successfully updated"),
        @ApiResponse(responseCode = "422", description = "Data is not valid")
    })
    @PutMapping(
        path = "/statuses/{id}",
        produces = "application/json"
    )
    public TaskStatus updateStatus(@PathVariable Long id, @Valid @RequestBody TaskStatusDto dto) {
        return taskStatusService.updateStatus(dto, id);
    }

    @Operation(summary = "Delete task status by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delete task status by id")
    })
    @DeleteMapping(
        path = "/statuses/{id}"
    )
    public void deleteStatus(@PathVariable Long id) {
        taskStatusRepository.deleteById(id);
    }
}
