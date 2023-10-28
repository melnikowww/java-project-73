package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;
    private final UserService userService;

    @Override
    public Task createTask(TaskDto dto) {
        return taskRepository.save(fromDto(dto));
    }

    @Override
    public Task updateTask(TaskDto dto, Long id) {
        Task realTask = taskRepository.findById(id).orElseThrow();
        merge(realTask, dto);
        return taskRepository.save(realTask);
    }

    @Override
    public List<Task> findWithFilter(Predicate predicate) {
        return predicate == null ? taskRepository.findAll() : taskRepository.findAll(predicate);
    }

    private Task fromDto(TaskDto dto) {
        final String name = dto.getName();
        final String desc = dto.getDescription();
        final User author = userService.getCurrentUser();
        final User executor = userRepository.findById(dto.getExecutorId()).orElseThrow();
        final TaskStatus taskStatus = taskStatusRepository.findById(dto.getTaskStatusId()).orElseThrow();
        final List<Label> labels = labelRepository.findByIdIn(dto.getLabelIds());
        return Task.builder()
            .name(name)
            .description(desc)
            .author(author)
            .executor(executor)
            .taskStatus(taskStatus)
            .labels(labels)
            .build();
    }

    private void merge(Task task, TaskDto dto) {
        Task newTask = fromDto(dto);

        task.setName(newTask.getName());
        task.setDescription(newTask.getDescription());
        task.setExecutor(newTask.getExecutor());
        task.setTaskStatus(newTask.getTaskStatus());
        task.setLabels(newTask.getLabels());
    }

}
