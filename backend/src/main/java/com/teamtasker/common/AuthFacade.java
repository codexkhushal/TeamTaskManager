package com.teamtasker.common;

import com.teamtasker.user.User;
import com.teamtasker.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthFacade {
  private final UserRepository userRepository;

  public AuthFacade(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getName() == null) {
      throw new IllegalArgumentException("Unauthenticated request");
    }
    return userRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new IllegalArgumentException("Authenticated user no longer exists"));
  }
}
