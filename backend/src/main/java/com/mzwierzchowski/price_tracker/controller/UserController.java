package com.mzwierzchowski.price_tracker.controller;

import com.mzwierzchowski.price_tracker.model.User;
import com.mzwierzchowski.price_tracker.model.dtos.UserDTO;
import com.mzwierzchowski.price_tracker.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<User> registerUser(@RequestBody UserDTO user) {
    User newUser = userService.addUser(user);
    return ResponseEntity.ok(newUser);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    User user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
    User user =
        userService.updateUser(
            id, updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getPassword());
    return ResponseEntity.ok(user);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
