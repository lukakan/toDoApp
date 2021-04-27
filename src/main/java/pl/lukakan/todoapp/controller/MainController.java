package pl.lukakan.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lukakan.todoapp.model.Task;
import pl.lukakan.todoapp.model.TaskStatus;
import pl.lukakan.todoapp.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    private final TaskRepository taskRepository;

    @Autowired
    public MainController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        List<Task> activeTasks = taskRepository.findByTaskStatusOrderByTargetDateAsc(TaskStatus.ACTIVE);
        List<Task> completedTasks = taskRepository.findByTaskStatusOrderByTargetDateAsc(TaskStatus.COMPLETED);
        LocalDate currentDate = LocalDate.now();
        model.addAttribute("activeTasks", activeTasks);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("currentDate", currentDate);
        return "home";
    }

    @GetMapping("/add")
    public String addTaskPage(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("editmode", false);
        model.addAttribute("action", "add");
        return "add";
    }

    @PostMapping("/add")
    public String addTask(Task task) {
        task.setTaskStatus(TaskStatus.ACTIVE);
        if (task.getTargetDate() == null) {
            task.setTargetDate(LocalDate.of(9999, 1, 1));
        }
        taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/update")
    public String editTaskPage(@RequestParam ("id") Long id, Model model) {
        Optional<Task> editTask = taskRepository.findById(id);
        model.addAttribute("task", editTask.get());
        model.addAttribute("editmode", true);
        model.addAttribute("action", "edit");
        return "add";
    }

    @PostMapping("/update")
    public String saveUpdatedTask(Task task, Model model) {
        if (task.getTargetDate() == null) {
            task.setTargetDate(LocalDate.of(9999, 1, 1));
        }
        taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/complete")
    public String markAsComplete(@RequestParam("id") Long id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        if (foundTask.isPresent()) {
            foundTask.get().setTaskStatus(TaskStatus.COMPLETED);
            taskRepository.save(foundTask.get());
        }
        return "redirect:/";
    }
}
