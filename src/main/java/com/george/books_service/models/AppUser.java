package com.george.books_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password; // Hashed password

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles; // List of roles (e.g., ROLE_ADMIN, ROLE_USER)

    // Default constructor
    public AppUser() {}

    // Constructor with parameters
    public AppUser(String username, String password, List<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
