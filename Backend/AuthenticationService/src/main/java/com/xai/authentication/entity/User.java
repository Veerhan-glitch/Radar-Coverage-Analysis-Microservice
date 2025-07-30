package com.xai.authentication.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users") // Maps this class to the "users" table in the database
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing primary key
    private Long id;

    @Column(length = 50, nullable = false, unique = true) // Unique username, required
    private String name;

    @Column(nullable = false) // Password is required
    private String password;

    private LocalDateTime createdAt; // Timestamp for when user was created

    public User() {}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    // Standard getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
