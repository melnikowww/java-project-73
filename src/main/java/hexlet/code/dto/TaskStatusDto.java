package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusDto {
    private String name;
    private Timestamp createdAt;

    public TaskStatusDto(String name) {
        this.name = name;
    }

}
