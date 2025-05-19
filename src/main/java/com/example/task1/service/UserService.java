package com.example.task1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.task1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(String name, String password) {
        boolean found = userRepository.findByNameAndPassword(name, password).isPresent();
        log.info("Repository findByNameAndPassword({}, {}) -> {}", name, password, found);
        return found;
    }
}
