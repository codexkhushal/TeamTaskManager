package com.teamtasker.auth;

import com.teamtasker.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {
  public record SignupRequest(
      @NotBlank String name,
      @Email @NotBlank String email,
      @NotBlank String password
  ) {}

  public record LoginRequest(
      @Email @NotBlank String email,
      @NotBlank String password
  ) {}

  public record AuthResponse(
      String token,
      Long id,
      String name,
      String email,
      Role role
  ) {}
}
