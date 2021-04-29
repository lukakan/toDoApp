package pl.lukakan.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lukakan.todoapp.service.TaskService;
import pl.lukakan.todoapp.model.Task;
import pl.lukakan.todoapp.model.TaskCategory;
import pl.lukakan.todoapp.model.TaskStatus;

import java.time.LocalDate;
import java.util.List;

@Controller
public class MainController {

    private final TaskService taskService;

    @Autowired
    public MainController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        List<Task> all = taskService.findAll();
        List<Task> activeTasks = taskService.findActive(all);
        List<Task> completedTasks = taskService.findCompleted(all);
        LocalDate currentDate = LocalDate.now();
        model.addAttribute("activeTasks", activeTasks);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("currentDate", currentDate);
        return "home";
    }

    @GetMapping("/add")
    public String addTaskPage(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("taskCategory", TaskCategory.values());
        model.addAttribute("editmode", false);
        model.addAttribute("action", "add");
        return "add";
    }

    @PostMapping("/add")
    public String addTask(Task task) {
        taskService.save(task);
        return "redirect:/";
    }

    @GetMapping("/update")
    public String editTaskPage(@RequestParam ("id") Long id, Model model) {
        Task editTask = taskService.findById(id);
        model.addAttribute("task", editTask);
        model.addAttribute("taskCategory", TaskCategory.values());
        model.addAttribute("taskStatuses", TaskStatus.values());
        model.addAttribute("editmode", true);
        model.addAttribute("action", "edit");
        return "add";
    }

    @PostMapping("/update")
    public String saveUpdatedTask(Task task, Model model) {
        taskService.save(task);
        return "redirect:/";
    }

    @GetMapping("/complete")
    public String markAsComplete(@RequestParam("id") Long id) {
        taskService.markAsCompleted(id);
        return "redirect:/";
    }
}
