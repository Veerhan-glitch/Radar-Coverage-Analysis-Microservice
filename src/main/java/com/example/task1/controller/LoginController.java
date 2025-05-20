package com.example.task1.controller;

import com.example.task1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UserService userService;

    // DTO to bind JSON for both login and register
    public static class UserDto {
        private String name;
        private String password;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    /**
     * POST /api/register
     * Body: { "name": "...", "password": "..." }
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserDto dto) {
        boolean created = userService.register(dto.getName(), dto.getPassword());
        if (!created) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Username already exists"));
        }
        return ResponseEntity.ok(Map.of("message", "User registered"));
    }

    /**
     * POST /api/login
     * Body: { "name": "...", "password": "..." }
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDto dto) {
        System.out.printf("Attempt login: name=%s, password=%s%n", dto.getName(), dto.getPassword());

        boolean ok = userService.authenticate(dto.getName(), dto.getPassword());
        if (ok) {
            return ResponseEntity.ok(Map.of("message", "Login successful"));
        } else {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"));
        }
    }
}
