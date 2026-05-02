package com.teamtasker.auth;

import com.teamtasker.user.User;
import com.teamtasker.user.Role;
import com.teamtasker.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
      AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  @Transactional
  public AuthDtos.AuthResponse signup(AuthDtos.SignupRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("Email is already registered");
    }

    User user = new User();
    user.setName(request.name());
    user.setEmail(request.email().toLowerCase());
    user.setPassword(passwordEncoder.encode(request.password()));
    user.setRole(userRepository.count() == 0 ? Role.ADMIN : Role.MEMBER);
    userRepository.save(user);
    return toResponse(user);
  }

  public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    User user = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
    return toResponse(user);
  }

  public AuthDtos.AuthResponse toResponse(User user) {
    return new AuthDtos.AuthResponse(
        jwtService.generateToken(user),
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getRole()
    );
  }
}
