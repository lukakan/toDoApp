package pl.lukakan.todoapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lukakan.todoapp.model.Task;
import pl.lukakan.todoapp.model.TaskStatus;
import pl.lukakan.todoapp.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void save(Task task) {
        if (task.getTargetDate() == null) {
            task.setTargetDate(LocalDate.of(9999, 1, 1));
        }
        if (task.getTaskStatus() == null) {
            task.setTaskStatus(TaskStatus.ACTIVE);
        }
        taskRepository.save(task);
    }

    public void markAsCompleted(Long id) {
        Task taskToComplete = findById(id);
        taskToComplete.setTaskStatus(TaskStatus.COMPLETED);
        taskRepository.save(taskToComplete);
    }

    public Task findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return task.get();
        } else {
            throw new NoSuchElementException("element with provided id doesn't exists");
        }
    }

    public List<Task> findAll() {
        return taskRepository.findAllByOrderByTargetDateAsc();
    }

    public List<Task> filterActive(List<Task> all) {
        return getTasksInGivenStatus(all, TaskStatus.ACTIVE);
    }

    public List<Task> filterCompleted(List<Task> all) {
        return getTasksInGivenStatus(all, TaskStatus.COMPLETED);
    }

    private List<Task> getTasksInGivenStatus(List<Task> all, TaskStatus active) {
        return all.stream()
                .filter(task -> task.getTaskStatus().equals(active))
                .collect(Collectors.toList());
    }
}
