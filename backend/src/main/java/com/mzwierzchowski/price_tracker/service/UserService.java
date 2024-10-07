package com.mzwierzchowski.price_tracker.service;

import com.mzwierzchowski.price_tracker.model.User;
import com.mzwierzchowski.price_tracker.model.dtos.UserDTO;
import com.mzwierzchowski.price_tracker.repository.UserRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User addUser(UserDTO newUser) {
    User user = new User();
    user.setUsername(newUser.getEmail());
    user.setPassword(passwordEncoder.encode(newUser.getPassword()));
    return userRepository.save(user);
  }

  public User getUserById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
  }

  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public void deleteUser(Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      userRepository.delete(user.get());
    } else {
      throw new RuntimeException("User not found with ID: " + id);
    }
  }

  public User updateUser(Long id, String username, String password) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

    user.setUsername(username);
    user.setPassword(password);

    return userRepository.save(user);
  }
}
