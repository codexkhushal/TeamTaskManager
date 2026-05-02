package com.teamtasker.user;

public class UserDtos {
  public record UserSummary(Long id, String name, String email, Role role) {}
}
