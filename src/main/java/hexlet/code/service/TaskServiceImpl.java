package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.config.security.JwtUtils;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskStatusRepository taskStatusRepository;
    @Autowired
    LabelRepository labelRepository;
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public Task createTask(TaskDto dto) {
        List<Label> labels = new ArrayList<>();
        if (dto.getLabelsId() != null) {
            for (Long labelId : dto.getLabelsId()) {
                labels.add(labelRepository.findById(labelId).orElseThrow());
            }
        }
        Task task = new Task(
            dto.getName(),
            dto.getDescription(),
            taskStatusRepository.findById(dto.getTaskStatusId()).orElseThrow(),
            userRepository.findById(dto.getAuthorId()).orElseThrow(),
            userRepository.findById(dto.getExecutorId()).orElseThrow(),
            labels
        );
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(TaskDto dto, Long id) {
        Task realTask = taskRepository.findById(id).orElseThrow();
        List<Label> labels = new ArrayList<>();
        if (dto.getLabelsId() != null) {
            for (Long labelId : dto.getLabelsId()) {
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
        return userRepository.findUserByEmail(jwtUtils.extractUsername(token.substring(7)))
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

    @Override
    public void deleteTask(Long id, String token) {
        if (isAuthor(token, id)) {
            taskRepository.deleteById(id);
        } else {
            throw new RuntimeException("Only author can delete this task! You can't!");
        }
    }

}
