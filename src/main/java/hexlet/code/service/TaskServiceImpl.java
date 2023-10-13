package hexlet.code.service;

import hexlet.code.config.security.JwtUtils;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    JwtUtils jwtUtils;

    @Override
    public Task createTask(TaskDto dto) {

        Task task = new Task(
            dto.getName(),
            dto.getDescription(),
            taskStatusRepository.findById(dto.getTaskStatusId()).orElseThrow(),
            userRepository.findById(dto.getAuthorId()).orElseThrow(),
            userRepository.findById(dto.getExecutorId()).orElseThrow()
        );
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(TaskDto dto, Long id) {
        Task realTask = taskRepository.findById(id).orElseThrow();
        Task updatedTask = new Task(
            dto.getName(),
            dto.getDescription(),
            taskStatusRepository.findById(dto.getTaskStatusId()).orElseThrow(),
            userRepository.findById(dto.getAuthorId()).orElseThrow(),
            userRepository.findById(dto.getExecutorId()).orElseThrow()
        );
        updatedTask.setId(realTask.getId());
        return taskRepository.save(updatedTask);
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
}
