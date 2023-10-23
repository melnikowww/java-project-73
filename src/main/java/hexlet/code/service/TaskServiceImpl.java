package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.config.security.JwtUtils;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;
    private final JwtUtils jwtUtils;

    @Override
    public Task createTask(TaskDto dto, Long id) {
        List<Label> labels = new ArrayList<>();
        if (dto.getLabelIds() != null) {
            for (Long labelId : dto.getLabelIds()) {
                labels.add(labelRepository.findById(labelId).orElseThrow());
            }
        }
        Task task = new Task();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setTaskStatus(taskStatusRepository.findById(dto.getTaskStatusId()).orElseThrow());
        task.setAuthor(userRepository.findById(id).orElseThrow());
        task.setExecutor(userRepository.findById(dto.getExecutorId()).orElseThrow());
        task.setLabels(labels);

        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(TaskDto dto, Long id) {
        Task realTask = taskRepository.findById(id).orElseThrow();
        List<Label> labels = new ArrayList<>();
        if (dto.getLabelIds() != null) {
            for (Long labelId : dto.getLabelIds()) {
                labels.add(labelRepository.findById(labelId).orElseThrow());
            }
        }
        realTask.setName(dto.getName());
        realTask.setDescription(dto.getDescription());
        realTask.setExecutor(userRepository.findById(dto.getExecutorId()).orElseThrow());
        realTask.setTaskStatus(taskStatusRepository.findById(dto.getTaskStatusId()).orElseThrow());
        realTask.setLabels(labels);
        return taskRepository.save(realTask);
    }

    @Override
    public Long findAuthorId(String token) {
        return userRepository.findUserByEmail(
            jwtUtils.extractUsername(token.substring(7))
            )
            .orElseThrow()
            .getId();
    }

    @Override
    public boolean isAuthor(String token, Long taskId) {
        Long userId = userRepository.findUserByEmail(jwtUtils.extractUsername(token.substring(7)))
            .orElseThrow()
            .getId();
        Long authorId = taskRepository.findById(taskId)
            .orElseThrow()
            .getAuthor()
            .getId();
        return Objects.equals(userId, authorId);
    }

    @Override
    public List<Task> findWithFilter(Predicate predicate) {
        return predicate == null ? taskRepository.findAll() : taskRepository.findAll(predicate);
    }

}
