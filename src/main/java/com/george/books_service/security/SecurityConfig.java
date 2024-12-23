package com.george.books_service.security;

import com.george.books_service.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    /**
     * Password encoder to securely hash and verify passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security filter chain to configure HTTP security.
     */
    // Security filter chain bean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Authorization rules
                .authorizeRequests(auth -> auth
                        .requestMatchers("/login/admindashboard.html").hasRole("ADMIN")  // Only "ADMIN" can access
                        .anyRequest().permitAll()  // Allow all other requests
                )
                // Form login configuration
                .formLogin(form -> form
                        .loginPage("/login/login.html")  // Custom login page
                        .loginProcessingUrl("/login")  // Login processing URL
                        .defaultSuccessUrl("/login/admindashboard.html", true)  // Redirect after successful login
                        .failureUrl("/login/login.html?error=true")  // Redirect after failed login
                        .permitAll()
                )
                // Logout behavior
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL for logout
                        .logoutSuccessUrl("/login/login.html?logout=true")  // Redirect after logout
                        .permitAll()
                )
                // Disable CSRF (in your case for simplicity)
                .csrf().disable()

                // Session management configuration
                .sessionManagement(session -> session
                        .maximumSessions(1)  // Limit to 1 session per user
                        .expiredUrl("/login/login.html?sessionExpired=true")  // Redirect if session expired
                );

        return http.build();
    }

    /**
     * UserDetailsService to load user details from the database.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userService.findByUsername(username)
                .map(user -> User.withUsername(user.getUsername())
                        .password(user.getPassword())  // Use hashed password
                        .roles(user.getRoles().toArray(new String[0]))  // Dynamically assign roles
                        .build())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
}
