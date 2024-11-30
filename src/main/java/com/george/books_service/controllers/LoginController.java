package com.george.books_service.controllers;

import com.george.books_service.dto.LoginRequest;
import com.george.books_service.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${user.username}")
    private String userUsername;

    @Value("${user.password}")
    private String userPassword;

    private boolean loggedIn = false;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Check if the provided username and password match the stored credentials
        if (adminUsername.equals(loginRequest.getUsername()) && adminPassword.equals(loginRequest.getPassword())) {
            loggedIn = true;
            return ResponseEntity.ok(new LoginResponse("ADMIN"));
        } else if (userUsername.equals(loginRequest.getUsername()) && userPassword.equals(loginRequest.getPassword())) {
            loggedIn = true;
            return ResponseEntity.ok(new LoginResponse("USER"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        if (loggedIn) {
            loggedIn = false;
            return ResponseEntity.ok("Successfully logged out");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not logged in");
        }
    }
}
