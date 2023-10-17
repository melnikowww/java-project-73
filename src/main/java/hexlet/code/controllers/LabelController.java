package hexlet.code.controllers;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
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
@Validated
@RequestMapping("${base.url}")
public class LabelController {

    @Autowired
    LabelRepository labelRepository;
    @Autowired
    LabelService labelService;

    @Operation(summary = "Get all labels")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of labels")
    })
    @GetMapping(
        path = "/labels",
        produces = "application/json"
    )
    public List<Label> getLabels() {
        return labelRepository.findAll();
    }

    @Operation(summary = "Get label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get specific label by id")
    })
    @GetMapping(
        path = "/labels/{id}",
        produces = "application/json"
    )
    public Label getLabel(@PathVariable Long id) {
        return labelRepository.findById(id).orElseThrow();
    }

    @Operation(summary = "Create new label")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Label is successfully created"),
        @ApiResponse(responseCode = "422", description = "Data is not valid")
    })
    @PostMapping(
        path = "/labels",
        produces = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Label createLabel(
        @Valid @RequestBody LabelDto dto) {
        return labelService.createLabel(dto);
    }

    @Operation(summary = "Update label")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Label is successfully updated"),
        @ApiResponse(responseCode = "422", description = "Data is not valid")
    })
    @PutMapping(
        path = "/labels/{id}",
        produces = "application/json"
    )
    public Label updateLabel(
        @Valid @RequestBody LabelDto dto,
        @PathVariable Long id) {
        return labelService.updateLabel(dto, id);
    }

    @Operation(summary = "Delete label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delete label by id")
    })
    @DeleteMapping(
        path = "/labels/{id}"
    )
    public void deleteLabel(@PathVariable Long id) {
        labelRepository.deleteById(id);
    }

}
