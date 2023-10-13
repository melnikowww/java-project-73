package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus createStatus(TaskStatusDto dto) {
        TaskStatus taskStatus = new TaskStatus(
            dto.getName()
        );
        return taskStatusRepository.save(taskStatus);
    }

    @Override
    public TaskStatus updateStatus(TaskStatusDto dto, Long id) {
        TaskStatus stat = taskStatusRepository.findById(id).orElseThrow();
        TaskStatus taskStatus = new TaskStatus(
            dto.getName()
        );
        taskStatus.setCreatedAt(stat.getCreatedAt());
        taskStatus.setId(id);
        return taskStatusRepository.save(taskStatus);
    }
}
