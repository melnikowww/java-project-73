package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskStatusDto {
    @Size(min = 1)
    @NotBlank
    private String name;

    public TaskStatusDto(String name) {
        this.name = name;
    }

}
