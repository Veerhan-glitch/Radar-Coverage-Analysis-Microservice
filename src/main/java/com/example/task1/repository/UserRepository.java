package com.example.task1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.task1.model.User;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByName(String name);
}
