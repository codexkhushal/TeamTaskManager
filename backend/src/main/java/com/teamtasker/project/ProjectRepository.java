package com.teamtasker.project;

import com.teamtasker.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
  List<Project> findDistinctByOwnerOrMembersContaining(User owner, User member);
}
