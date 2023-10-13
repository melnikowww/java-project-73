package hexlet.code.controllers;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/statuses")
@Validated
public class TaskStatusController {

    @Autowired
    TaskStatusRepository taskStatusRepository;
    @Autowired
    TaskStatusService taskStatusService;
    @Autowired
    TaskRepository taskRepository;



    @GetMapping(
        path = "",
        produces = "application/json"
    )
    public ResponseEntity<List<TaskStatus>> getStatuses() {
        return ResponseEntity.ok(taskStatusRepository.findAll());
    }

    @GetMapping(
        path = "/{id}",
        produces = "application/json"
    )
    public ResponseEntity<TaskStatus> getStatus(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(taskStatusRepository.findById(id).orElseThrow());
    }

    @PostMapping(
        path = "",
        produces = "application/json"
    )
    public ResponseEntity<TaskStatus> createStatus(@Valid @RequestBody TaskStatusDto dto) {
        return ResponseEntity.status(201).body(taskStatusService.createStatus(dto));
    }

    @PutMapping(
        path = "/{id}",
        produces = "application/json"
    )
    public ResponseEntity<TaskStatus> updateStatus(@PathVariable Long id, @Valid @RequestBody TaskStatusDto dto) {
        return ResponseEntity.status(200).body(taskStatusService.updateStatus(dto, id));
    }

    @DeleteMapping(
        path = "/{id}"
    )
    public void deleteStatus(@PathVariable Long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No one status was found!"));
        if (taskRepository.findByTaskStatus(taskStatus).isEmpty()) {
            taskStatusRepository.deleteById(id);
        }
    }
}
