package com.teamtasker.task;

import com.teamtasker.common.AuthFacade;
import com.teamtasker.project.Project;
import com.teamtasker.project.ProjectService;
import com.teamtasker.user.Role;
import com.teamtasker.user.User;
import com.teamtasker.user.UserDtos;
import com.teamtasker.user.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
  private final TaskRepository taskRepository;
  private final UserRepository userRepository;
  private final ProjectService projectService;
  private final AuthFacade authFacade;

  public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProjectService projectService,
      AuthFacade authFacade) {
    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
    this.projectService = projectService;
    this.authFacade = authFacade;
  }

  @Transactional(readOnly = true)
  public List<TaskDtos.TaskResponse> listTasks() {
    User currentUser = authFacade.currentUser();
    List<Task> tasks = currentUser.getRole() == Role.ADMIN
        ? taskRepository.findAll()
        : taskRepository.findByAssignee(currentUser);
    return tasks.stream().map(this::toResponse).toList();
  }

  @Transactional
  public TaskDtos.TaskResponse create(TaskDtos.TaskRequest request) {
    requireAdmin();
    Project project = projectService.findAccessibleProject(request.projectId());
    User assignee = findUser(request.assigneeId());
    ensureMember(project, assignee);

    Task task = new Task();
    task.setTitle(request.title());
    task.setDescription(request.description());
    task.setProject(project);
    task.setAssignee(assignee);
    task.setDeadline(request.deadline());
    task.setStatus(request.status() == null ? TaskStatus.TODO : request.status());
    return toResponse(taskRepository.save(task));
  }

  @Transactional
  public TaskDtos.TaskResponse updateStatus(Long taskId, TaskDtos.TaskStatusRequest request) {
    Task task = findTask(taskId);
    User currentUser = authFacade.currentUser();
    boolean allowed = currentUser.getRole() == Role.ADMIN || task.getAssignee().getId().equals(currentUser.getId());
    if (!allowed) {
      throw new IllegalArgumentException("You cannot update this task");
    }
    task.setStatus(request.status());
    return toResponse(taskRepository.save(task));
  }

  @Transactional
  public void delete(Long taskId) {
    Task task = findTask(taskId);
    User currentUser = authFacade.currentUser();
    boolean allowed = currentUser.getRole() == Role.ADMIN || task.getAssignee().getId().equals(currentUser.getId());
    if (!allowed) {
      throw new IllegalArgumentException("You cannot delete this task");
    }
    taskRepository.delete(task);
  }

  private Task findTask(Long id) {
    return taskRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Task not found"));
  }

  private User findUser(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
  }

  private void ensureMember(Project project, User assignee) {
    boolean assignedToMember = project.getMembers().stream().anyMatch(member -> member.getId().equals(assignee.getId()));
    if (!assignedToMember) {
      throw new IllegalArgumentException("Assignee must be a member of the selected project");
    }
  }

  private void requireAdmin() {
    if (authFacade.currentUser().getRole() != Role.ADMIN) {
      throw new IllegalArgumentException("Admin access required");
    }
  }

  private TaskDtos.TaskResponse toResponse(Task task) {
    User user = task.getAssignee();
    return new TaskDtos.TaskResponse(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        task.getStatus(),
        task.getDeadline(),
        task.getProject().getId(),
        task.getProject().getName(),
        new UserDtos.UserSummary(user.getId(), user.getName(), user.getEmail(), user.getRole())
    );
  }
}
