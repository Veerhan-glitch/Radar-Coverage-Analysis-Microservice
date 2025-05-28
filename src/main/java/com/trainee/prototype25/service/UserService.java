package com.trainee.prototype25.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trainee.prototype25.model.User;
import com.trainee.prototype25.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(String name, String rawPassword) {
        return userRepository.findByName(name)
                .map(user -> user.getPassword().equals(rawPassword))
                .orElse(false);
    }

    public boolean register(String name, String password) {
        if (userRepository.existsByName(name)) return false;
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setCreatedAt(LocalDateTime.now());
        } else {
            user.setUpdatedAt(LocalDateTime.now());
        }
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }
}
