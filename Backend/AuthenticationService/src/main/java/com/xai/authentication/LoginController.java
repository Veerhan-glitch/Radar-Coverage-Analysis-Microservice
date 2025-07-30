package com.xai.authentication;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") // Allows frontend at port 4200 to access this API
public class LoginController {

    @Autowired
    private UserService userService;

    // DTO class for receiving login/register requests
    public static class UserDto {
        private String name;
        private String password;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // Endpoint to register a new user
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserDto dto) {
        boolean created = userService.register(dto.getName(), dto.getPassword());
        if (!created) {
            // Username already exists
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Username already exists"));
        }
        return ResponseEntity.ok(Map.of("message", "User registered"));
    }

    // Endpoint to authenticate user login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto dto) {
        return userService.findByName(dto.getName())
            .filter(user -> user.getPassword().equals(dto.getPassword())) // Validate password
            .map(user -> Map.of(
                "message", "Login successful",
                "user", Map.of(
                    "id", user.getId(),
                    "name", user.getName()
                )
            ))
            .map(ResponseEntity::ok) // Return success response
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"))); // If user not found or wrong password
    }

}
