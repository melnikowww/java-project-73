package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;

public interface TaskStatusService {

    TaskStatus createStatus(TaskStatusDto dto);
    TaskStatus updateStatus(TaskStatusDto dto, Long id);

}
