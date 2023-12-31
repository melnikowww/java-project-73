package hexlet.code.repository;

import com.querydsl.core.types.Predicate;
import hexlet.code.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, QuerydslPredicateExecutor<Task> {
    Optional<Task> findByName(String name);
    List<Task> findAll(Predicate predicate);
    List<Task> findAll();
}
