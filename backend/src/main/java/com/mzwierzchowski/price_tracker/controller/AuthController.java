package com.mzwierzchowski.price_tracker.controller;

import com.mzwierzchowski.price_tracker.config.JwtUtils;
import com.mzwierzchowski.price_tracker.model.dtos.AuthRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // Pozwala na połączenia z portu 3000 (frontend)
public class AuthController {

  private AuthenticationManager authenticationManager;
  private JwtUtils jwtUtils;

  public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/login")
  public String login(@RequestBody AuthRequest authRequest) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  authRequest.getUsername(), authRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      System.out.println("user successfully logged in");

      return jwtUtils.generateToken(authRequest.getUsername());

    } catch (AuthenticationException e) {
      throw new RuntimeException("Niepoprawne dane logowania");
    }
  }
}
