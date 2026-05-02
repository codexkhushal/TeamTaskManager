package com.teamtasker.common;

import com.teamtasker.task.TaskRepository;
import com.teamtasker.task.TaskStatus;
import com.teamtasker.user.Role;
import com.teamtasker.user.User;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
  private final TaskRepository taskRepository;
  private final AuthFacade authFacade;

  public DashboardController(TaskRepository taskRepository, AuthFacade authFacade) {
    this.taskRepository = taskRepository;
    this.authFacade = authFacade;
  }

  @GetMapping
  public Map<String, Long> summary() {
    User currentUser = authFacade.currentUser();
    if (currentUser.getRole() == Role.ADMIN) {
      return Map.of(
          "totalTasks", taskRepository.count(),
          "completedTasks", taskRepository.countByStatus(TaskStatus.DONE),
          "pendingTasks", taskRepository.countByStatus(TaskStatus.TODO) + taskRepository.countByStatus(TaskStatus.IN_PROGRESS),
          "overdueTasks", taskRepository.countByDeadlineBeforeAndStatusNot(LocalDate.now(), TaskStatus.DONE)
      );
    }

    return Map.of(
        "totalTasks", taskRepository.countByAssignee(currentUser),
        "completedTasks", taskRepository.countByAssigneeAndStatus(currentUser, TaskStatus.DONE),
        "pendingTasks", taskRepository.countByAssigneeAndStatus(currentUser, TaskStatus.TODO)
            + taskRepository.countByAssigneeAndStatus(currentUser, TaskStatus.IN_PROGRESS),
        "overdueTasks", taskRepository.findByAssignee(currentUser).stream()
            .filter(task -> task.getDeadline() != null
                && task.getDeadline().isBefore(LocalDate.now())
                && task.getStatus() != TaskStatus.DONE)
            .count()
    );
  }
}
