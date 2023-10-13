package hexlet.code.controllers;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/api/tasks")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskService taskService;
    @Autowired
    UserRepository userRepository;

    @GetMapping(
        path = "",
        produces = "application/json"
    )
    public ResponseEntity<List<Task>> getTasks() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping(
        path = "/{id}",
        produces = "application/json"
    )
    public ResponseEntity<Task> getTasks(@PathVariable Long id) {
        return ResponseEntity.ok(taskRepository.findById(id).orElseThrow());
    }

    @PostMapping(
        path = "",
        produces = "application/json"
    )
    public ResponseEntity<Task> createTask(
        @Valid @RequestBody TaskDto dto,
        @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        dto.setAuthorId(taskService.findAuthorId(token));
        return ResponseEntity.status(201).body(taskService.createTask(dto));
    }

    @PutMapping(
        path = "/{id}",
        produces = "application/json"
    )
    public ResponseEntity<Task> updateTask(
        @Valid @RequestBody TaskDto dto,
        @PathVariable Long id) {
        return ResponseEntity.status(200).body(taskService.updateTask(dto, id));
    }

    @DeleteMapping(
        path = "/{id}"
    )
    public void deleteTask(
        @PathVariable Long id,
        @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        if (taskService.isAuthor(token, id)) {
            taskRepository.deleteById(id);
        } else {
            throw new RuntimeException("Only author can delete this task! You can't!");
        }
    }
}
