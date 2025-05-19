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

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDto dto) {
        String name = dto.getName();
        String pwd  = dto.getPassword();
        System.out.printf("Attempt login: name=%s, password=%s%n", name, pwd);  // debug

        boolean ok = userService.authenticate(name, pwd);

        if (ok) {
            System.err.println(ok);
            System.err.println("Login successful");
            return ResponseEntity.ok(Map.of("message", "Login successful"));
        } else {
            System.err.println(ok);
            System.err.println("Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }

    // DTO to bind JSON
    public static class UserDto {
        private String name;
        private String password;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
