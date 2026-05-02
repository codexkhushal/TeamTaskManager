package com.teamtasker.project;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
  private final ProjectService projectService;

  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @GetMapping
  public List<ProjectDtos.ProjectResponse> listProjects() {
    return projectService.listProjects();
  }

  @PostMapping
  public ProjectDtos.ProjectResponse create(@Valid @RequestBody ProjectDtos.ProjectRequest request) {
    return projectService.create(request);
  }

  @PutMapping("/{projectId}")
  public ProjectDtos.ProjectResponse update(@PathVariable Long projectId,
      @Valid @RequestBody ProjectDtos.ProjectRequest request) {
    return projectService.update(projectId, request);
  }

  @PutMapping("/{projectId}/members")
  public ProjectDtos.ProjectResponse updateMembers(@PathVariable Long projectId,
      @RequestBody ProjectDtos.MemberUpdateRequest request) {
    return projectService.updateMembers(projectId, request);
  }
}
