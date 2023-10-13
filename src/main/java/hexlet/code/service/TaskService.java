package hexlet.code.service;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;

public interface TaskService {

    Task createTask(TaskDto dto);
    Task updateTask(TaskDto dto, Long id);
    Long findAuthorId(String token);
    boolean isAuthor(String token, Long id);
}
