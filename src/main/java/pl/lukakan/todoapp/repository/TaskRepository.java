package pl.lukakan.todoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lukakan.todoapp.model.Task;
import pl.lukakan.todoapp.model.TaskStatus;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    public List<Task> findByTaskStatusOrderByTargetDateAsc(TaskStatus taskStatus);
}
