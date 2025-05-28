package com.trainee.prototype25.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection
    private List<String> armyOptions;

    @ElementCollection
    private List<String> navyOptions;

    @ElementCollection
    private List<String> airforceOptions;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<String> getArmyOptions() { return armyOptions; }
    public void setArmyOptions(List<String> armyOptions) { this.armyOptions = armyOptions; }

    public List<String> getNavyOptions() { return navyOptions; }
    public void setNavyOptions(List<String> navyOptions) { this.navyOptions = navyOptions; }

    public List<String> getAirforceOptions() { return airforceOptions; }
    public void setAirforceOptions(List<String> airforceOptions) { this.airforceOptions = airforceOptions; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
