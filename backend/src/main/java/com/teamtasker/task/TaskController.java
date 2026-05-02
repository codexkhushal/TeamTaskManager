package com.teamtasker.task;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public List<TaskDtos.TaskResponse> listTasks() {
    return taskService.listTasks();
  }

  @PostMapping
  public TaskDtos.TaskResponse create(@Valid @RequestBody TaskDtos.TaskRequest request) {
    return taskService.create(request);
  }

  @PatchMapping("/{taskId}/status")
  public TaskDtos.TaskResponse updateStatus(@PathVariable Long taskId,
      @Valid @RequestBody TaskDtos.TaskStatusRequest request) {
    return taskService.updateStatus(taskId, request);
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<Void> delete(@PathVariable Long taskId) {
    taskService.delete(taskId);
    return ResponseEntity.noContent().build();
  }
}
