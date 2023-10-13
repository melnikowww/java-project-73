package hexlet.code.controllers;

import hexlet.code.dto.LabelDto;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.LabelService;
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

@RestController
@Validated
@RequestMapping("/api/labels")
public class LabelController {

    @Autowired
    LabelRepository labelRepository;
    @Autowired
    LabelService labelService;
    @Autowired
    TaskRepository taskRepository;

    @GetMapping(
        path = "",
        produces = "application/json"
    )
    public ResponseEntity<List<Label>> getLabels() {
        return ResponseEntity.ok(labelRepository.findAll());
    }

    @GetMapping(
        path = "/{id}",
        produces = "application/json"
    )
    public ResponseEntity<Label> getLabel(@PathVariable Long id) {
        return ResponseEntity.ok(labelRepository.findById(id).orElseThrow());
    }

    @PostMapping(
        path = "",
        produces = "application/json"
    )
    public ResponseEntity<Label> createLabel(
        @Valid @RequestBody LabelDto dto) {
        return ResponseEntity.status(201).body(labelService.createLabel(dto));
    }

    @PutMapping(
        path = "/{id}",
        produces = "application/json"
    )
    public ResponseEntity<Label> updateLabel(
        @Valid @RequestBody LabelDto dto,
        @PathVariable Long id) {
        return ResponseEntity.status(200).body(labelService.updateLabel(dto, id));
    }

    @DeleteMapping(
        path = "/{id}"
    )
    public void deleteLabel(@PathVariable int labels_id) {
        if (taskRepository.findAllByLabelsContaining(labels_id).isEmpty()) {
//            labelService.deleteLabel(labels_id);
        } else {
            throw new RuntimeException("This label is referenced by some task");
        }
    }

}
