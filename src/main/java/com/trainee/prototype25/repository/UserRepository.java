package com.trainee.prototype25.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.trainee.prototype25.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    boolean existsByName(String name);
}
