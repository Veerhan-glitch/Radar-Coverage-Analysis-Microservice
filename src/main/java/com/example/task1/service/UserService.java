package com.example.task1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.task1.model.User;
import com.example.task1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(String name, String password) {
        boolean found = userRepository.findByName(name)
            .map(user -> user.getPassword().equals(password))
            .orElse(false);

        log.info("Authentication attempt for user '{}': {}", name, found);
        return found;
    }

    public boolean register(String name, String password) {
        if (userRepository.existsByName(name)) {
            log.warn("Attempt to register already existing user '{}'", name);
            return false;
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setPassword(password); // Consider hashing the password in production
        userRepository.save(newUser);
        
        log.info("Registered new user '{}'", name);
        return true;
    }
}
