package com.mzwierzchowski.price_tracker.service;

import com.mzwierzchowski.price_tracker.model.User;
import com.mzwierzchowski.price_tracker.model.dtos.UserDTO;
import com.mzwierzchowski.price_tracker.repository.UserRepository;
import java.util.List;
import java.util.Optional;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserService {

  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User addUser(UserDTO newUser) {
    log.info("Adding new user with email: {}", newUser.getEmail());
    User user = new User();
    user.setUsername(newUser.getEmail());
    user.setPassword(passwordEncoder.encode(newUser.getPassword()));

    User savedUser = userRepository.save(user);
    log.info("User with email {} successfully added with ID: {}", newUser.getEmail(), savedUser.getId());

    return savedUser;
  }

  public User getUserById(Long id) {
    log.info("Fetching user with ID: {}", id);
    return userRepository
            .findById(id)
            .orElseThrow(() -> {
              log.error("User not found with ID: {}", id);
              return new RuntimeException("User not found with ID: " + id);
            });
  }

  public User getUserByUsername(String username) {
    log.info("Fetching user with username: {}", username);
    User user = userRepository.findByUsername(username);
    if (user == null) {
      log.warn("User with username {} not found", username);
    } else {
      log.info("User with username {} found", username);
    }
    return user;
  }

  public List<User> getAllUsers() {
    log.info("Fetching all users");
    List<User> users = userRepository.findAll();
    log.info("Total users found: {}", users.size());
    return users;
  }

  public void deleteUser(Long id) {
    log.info("Attempting to delete user with ID: {}", id);
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      userRepository.delete(user.get());
      log.info("User with ID {} successfully deleted", id);
    } else {
      log.error("User not found with ID: {}", id);
      throw new RuntimeException("User not found with ID: " + id);
    }
  }

  public User updateUser(Long id, String username, String password) {
    log.info("Attempting to update user with ID: {}", id);
    User user = userRepository
            .findById(id)
            .orElseThrow(() -> {
              log.error("User not found with ID: {}", id);
              return new RuntimeException("User not found with ID: " + id);
            });

    log.info("Updating user with ID {}: new username = {}, new password = [PROTECTED]", id, username);
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));

    User updatedUser = userRepository.save(user);
    log.info("User with ID {} successfully updated", id);
    return updatedUser;
  }
}
