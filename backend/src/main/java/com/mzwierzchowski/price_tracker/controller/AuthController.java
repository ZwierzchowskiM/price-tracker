package com.mzwierzchowski.price_tracker.controller;

import com.mzwierzchowski.price_tracker.config.JwtUtils;
import com.mzwierzchowski.price_tracker.model.dtos.AuthRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "https://price-tracker-one-delta.vercel.app"})
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;

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
      log.info("user successfully logged in");

      return jwtUtils.generateToken(authRequest.getUsername());

    } catch (AuthenticationException e) {
      throw new RuntimeException("Niepoprawne dane logowania");
    }
  }
}
