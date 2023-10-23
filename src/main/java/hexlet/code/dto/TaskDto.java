package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class TaskDto {
    private @NotBlank @Size(min = 3, max = 1000) String name;

    private String description;

    private Long executorId;

    private @NotNull Long taskStatusId;

    private Set<Long> labelIds;

}
