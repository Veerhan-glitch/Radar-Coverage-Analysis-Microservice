package com.xai.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xai.authentication.entity.User;
import com.xai.authentication.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; // Injects the JPA repository for User entity

    // Verifies username and password match
    public boolean authenticate(String name, String rawPassword) {
        return userRepository.findByName(name)
                .map(user -> user.getPassword().equals(rawPassword)) // Simple equality check (no hashing)
                .orElse(false); // If user not found, return false
    }

    // Registers a new user if the username doesn't already exist
    public boolean register(String name, String password) {
        if (userRepository.existsByName(name)) return false; // Prevent duplicate usernames
        User user = new User();
        user.setName(name);
        user.setPassword(password); // Stores raw password (should be hashed in production)
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user); // Persist user in database
        return true;
    }

    // Returns user by name (wrapped in Optional)
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }
}
