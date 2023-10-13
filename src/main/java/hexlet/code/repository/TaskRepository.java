package hexlet.code.repository;

import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByName(String name);
    Optional<Task> findByAuthor(User user);
    Optional<Task> findByTaskStatus(TaskStatus taskStatus);

    @Query(value = "SELECT *  FROM tasks_labels WHERE labels_id = ?1", nativeQuery = true)
    List<Task> findAllByLabelsContaining(int labels_id);
}
