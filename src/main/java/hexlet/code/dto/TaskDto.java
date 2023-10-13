package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private String name;
    private String description;
    private Long taskStatusId;
    private Long authorId;
    private Long executorId;
    private Set<Long> labelIds;

    public TaskDto(
        String name,
        String description,
        Long taskStatusId,
        Long executorId,
        Set<Long> labelIds
    ) {
        this.name = name;
        this.description = description;
        this.taskStatusId = taskStatusId;
        this.executorId = executorId;
        this.labelIds = labelIds;
    }
}
