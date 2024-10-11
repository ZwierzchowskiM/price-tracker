package com.mzwierzchowski.price_tracker.controller;

import com.mzwierzchowski.price_tracker.model.User;
import com.mzwierzchowski.price_tracker.model.dtos.UserDTO;
import com.mzwierzchowski.price_tracker.service.UserService;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<User> registerUser(@RequestBody UserDTO user) {
    log.info("Attempting to register new user with email: {}", user.getEmail());
    User newUser = userService.addUser(user);
    log.info("Successfully registered user with ID: {}", newUser.getId());
    return ResponseEntity.ok(newUser);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    log.info("Fetching user with ID: {}", id);
    User user = userService.getUserById(id);
    log.info("Successfully fetched user with ID: {}", user.getId());
    return ResponseEntity.ok(user);
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    log.info("Fetching all users");
    List<User> users = userService.getAllUsers();
    log.info("Successfully fetched {} users", users.size());
    return ResponseEntity.ok(users);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    log.info("Attempting to delete user with ID: {}", id);
    userService.deleteUser(id);
    log.info("Successfully deleted user with ID: {}", id);
    return ResponseEntity.noContent().build();
  }
}
