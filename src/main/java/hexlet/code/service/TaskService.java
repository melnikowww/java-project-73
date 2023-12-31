package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;

import java.util.List;

public interface TaskService {

    Task createTask(TaskDto dto);
    Task updateTask(TaskDto dto, Long id);
    List<Task> findWithFilter(Predicate predicate);
}
