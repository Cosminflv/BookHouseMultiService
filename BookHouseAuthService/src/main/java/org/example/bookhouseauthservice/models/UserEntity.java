package org.example.bookhouseauthservice.models;

import jakarta.persistence.*;

@Entity
@Table(name = "user_entity")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID
    private Long id;

    @Column(name = "username" , unique = true, nullable = false)
    private String username;
    private String password;

    private EUserType userType;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public EUserType getUserType() { return userType; }
    public void setUserType(EUserType userType) { this.userType = userType; }
}
