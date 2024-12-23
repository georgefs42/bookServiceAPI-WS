package com.george.books_service.controllers;

import com.george.books_service.models.AppUser;
import com.george.books_service.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users") // Base URL for all user-related endpoints
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Get a user by username
    @GetMapping("/{username}")
    public ResponseEntity<AppUser> getUserByUsername(@PathVariable String username) {
        Optional<AppUser> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add a new user to the system
    @PostMapping
    public ResponseEntity<AppUser> createUser(@RequestBody @Valid AppUser appUser) {
        // Hash the password before saving it
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        AppUser createdUser = userService.saveUser(appUser);
        return ResponseEntity.status(201).body(createdUser);
    }

    // Update an existing user's details
    @PutMapping("/{username}")
    public ResponseEntity<AppUser> updateUser(@PathVariable String username, @RequestBody @Valid AppUser appUserDetails) {
        Optional<AppUser> existingUser = userService.findByUsername(username);
        if (existingUser.isPresent()) {
            AppUser updatedUser = existingUser.get();
            updatedUser.setUsername(appUserDetails.getUsername());
            updatedUser.setPassword(passwordEncoder.encode(appUserDetails.getPassword())); // Ensure the password is hashed
            updatedUser.setRoles(appUserDetails.getRoles());
            userService.saveUser(updatedUser);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete a user by username
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        Optional<AppUser> user = userService.findByUsername(username);
        if (user.isPresent()) {
            userService.deleteUser(user.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // New login endpoint
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String username, @RequestParam String password) {
        Optional<AppUser> user = userService.findByUsername(username);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            // Successful login
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            return ResponseEntity.ok(response); // Return response as JSON
        } else {
            // Invalid login
            Map<String, String> response = new HashMap<> ();
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(401).body(response); // Return response as JSON with 401
        }
    }

}
