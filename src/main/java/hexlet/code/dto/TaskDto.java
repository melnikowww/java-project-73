package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    @NotBlank
    @Size(min = 1)
    private String name;
    private String description;
    private Long taskStatusId;
    private Long authorId;
    private Long executorId;
    private List<Long> labelsId;

    public TaskDto(
        String name,
        String description,
        Long taskStatusId,
        Long executorId,
        List<Long> labelsId
    ) {
        this.name = name;
        this.description = description;
        this.taskStatusId = taskStatusId;
        this.executorId = executorId;
        this.labelsId = labelsId;
    }
}
