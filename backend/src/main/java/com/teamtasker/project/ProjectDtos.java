package com.teamtasker.project;

import com.teamtasker.user.UserDtos;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

public class ProjectDtos {
  public record ProjectRequest(
      @NotBlank String name,
      String description,
      LocalDate deadline,
      List<Long> memberIds
  ) {}

  public record MemberUpdateRequest(List<Long> memberIds) {}

  public record ProjectResponse(
      Long id,
      String name,
      String description,
      LocalDate deadline,
      UserDtos.UserSummary owner,
      List<UserDtos.UserSummary> members
  ) {}
}
