package com.teamtasker.task;

import com.teamtasker.user.UserDtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class TaskDtos {
  public record TaskRequest(
      @NotBlank String title,
      String description,
      @NotNull Long projectId,
      @NotNull Long assigneeId,
      LocalDate deadline,
      TaskStatus status
  ) {}

  public record TaskStatusRequest(@NotNull TaskStatus status) {}

  public record TaskResponse(
      Long id,
      String title,
      String description,
      TaskStatus status,
      LocalDate deadline,
      Long projectId,
      String projectName,
      UserDtos.UserSummary assignee
  ) {}
}
