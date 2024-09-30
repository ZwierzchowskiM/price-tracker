package com.mzwierzchowski.price_tracker.repository;

import com.mzwierzchowski.price_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
