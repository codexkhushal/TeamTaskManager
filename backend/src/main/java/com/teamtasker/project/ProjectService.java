package com.teamtasker.project;

import com.teamtasker.common.AuthFacade;
import com.teamtasker.user.Role;
import com.teamtasker.user.User;
import com.teamtasker.user.UserDtos;
import com.teamtasker.user.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;
  private final AuthFacade authFacade;

  public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, AuthFacade authFacade) {
    this.projectRepository = projectRepository;
    this.userRepository = userRepository;
    this.authFacade = authFacade;
  }

  @Transactional(readOnly = true)
  public List<ProjectDtos.ProjectResponse> listProjects() {
    User currentUser = authFacade.currentUser();
    List<Project> projects = currentUser.getRole() == Role.ADMIN
        ? projectRepository.findAll()
        : projectRepository.findDistinctByOwnerOrMembersContaining(currentUser, currentUser);
    return projects.stream().map(this::toResponse).toList();
  }

  @Transactional
  public ProjectDtos.ProjectResponse create(ProjectDtos.ProjectRequest request) {
    User currentUser = requireAdmin();
    Project project = new Project();
    project.setName(request.name());
    project.setDescription(request.description());
    project.setDeadline(request.deadline());
    project.setOwner(currentUser);
    project.setMembers(resolveMembers(request.memberIds(), currentUser));
    return toResponse(projectRepository.save(project));
  }

  @Transactional
  public ProjectDtos.ProjectResponse update(Long projectId, ProjectDtos.ProjectRequest request) {
    requireAdmin();
    Project project = findProject(projectId);
    project.setName(request.name());
    project.setDescription(request.description());
    project.setDeadline(request.deadline());
    project.setMembers(resolveMembers(request.memberIds(), project.getOwner()));
    return toResponse(projectRepository.save(project));
  }

  @Transactional
  public ProjectDtos.ProjectResponse updateMembers(Long projectId, ProjectDtos.MemberUpdateRequest request) {
    requireAdmin();
    Project project = findProject(projectId);
    project.setMembers(resolveMembers(request.memberIds(), project.getOwner()));
    return toResponse(projectRepository.save(project));
  }

  @Transactional
  public void delete(Long projectId) {
    requireAdmin();
    Project project = findProject(projectId);
    projectRepository.delete(project);
  }

  @Transactional(readOnly = true)
  public Project findAccessibleProject(Long id) {
    Project project = findProject(id);
    User currentUser = authFacade.currentUser();
    boolean allowed = currentUser.getRole() == Role.ADMIN
        || project.getOwner().getId().equals(currentUser.getId())
        || project.getMembers().stream().anyMatch(member -> member.getId().equals(currentUser.getId()));
    if (!allowed) {
      throw new IllegalArgumentException("You do not have access to this project");
    }
    return project;
  }

  private Project findProject(Long id) {
    return projectRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Project not found"));
  }

  private User requireAdmin() {
    User currentUser = authFacade.currentUser();
    if (currentUser.getRole() != Role.ADMIN) {
      throw new IllegalArgumentException("Admin access required");
    }
    return currentUser;
  }

  private Set<User> resolveMembers(List<Long> memberIds, User owner) {
    Set<User> members = new HashSet<>();
    members.add(owner);
    if (memberIds != null && !memberIds.isEmpty()) {
      members.addAll(userRepository.findAllById(memberIds));
    }
    return members;
  }

  ProjectDtos.ProjectResponse toResponse(Project project) {
    return new ProjectDtos.ProjectResponse(
        project.getId(),
        project.getName(),
        project.getDescription(),
        project.getDeadline(),
        toUserSummary(project.getOwner()),
        project.getMembers().stream().map(this::toUserSummary).toList()
    );
  }

  private UserDtos.UserSummary toUserSummary(User user) {
    return new UserDtos.UserSummary(user.getId(), user.getName(), user.getEmail(), user.getRole());
  }
}
