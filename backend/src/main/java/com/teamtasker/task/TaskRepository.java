package com.teamtasker.task;

import com.teamtasker.project.Project;
import com.teamtasker.user.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByAssignee(User assignee);
  List<Task> findByProject(Project project);
  long countByAssignee(User assignee);
  long countByAssigneeAndStatus(User assignee, TaskStatus status);
  long countByStatus(TaskStatus status);
  long countByDeadlineBeforeAndStatusNot(LocalDate date, TaskStatus status);
}
