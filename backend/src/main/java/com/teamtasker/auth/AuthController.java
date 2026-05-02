package com.teamtasker.auth;

import com.teamtasker.common.AuthFacade;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;
  private final AuthFacade authFacade;

  public AuthController(AuthService authService, AuthFacade authFacade) {
    this.authService = authService;
    this.authFacade = authFacade;
  }

  @PostMapping("/signup")
  public AuthDtos.AuthResponse signup(@Valid @RequestBody AuthDtos.SignupRequest request) {
    return authService.signup(request);
  }

  @PostMapping("/login")
  public AuthDtos.AuthResponse login(@Valid @RequestBody AuthDtos.LoginRequest request) {
    return authService.login(request);
  }

  @GetMapping("/me")
  public AuthDtos.AuthResponse me() {
    return authService.toResponse(authFacade.currentUser());
  }
}
