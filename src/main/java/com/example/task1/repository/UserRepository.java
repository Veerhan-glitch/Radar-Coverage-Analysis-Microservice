package com.example.task1.repository;

import com.example.task1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.name = :name AND u.password = :password")
    Optional<User> findByNameAndPassword(
        @Param("name") String name,
        @Param("password") String password
    );
}
