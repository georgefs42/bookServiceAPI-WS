package com.george.books_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${app.username}")
    private String username;

    @Value("${app.password}")
    private String password;

    // Bean to handle password encoding (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Define authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/admindashboard.html").hasRole("ADMIN")  // Protect admin page: Only accessible by users with "ADMIN" role
                        .anyRequest().permitAll()  // Allow access to all other pages without authentication
                )
                // Define login form configuration
                .formLogin(form -> form
                        .loginPage("/login/login.html") // Specify the custom login page
                        .loginProcessingUrl("/login") // URL to submit the login form
                        .defaultSuccessUrl("/admin/admindashboard.html", true) // Redirect on successful login
                        .failureUrl("/login/login.html?error=true") // Redirect on failed login
                        .permitAll()
                )
                // Configure logout behavior
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL for logout
                        .logoutSuccessUrl("/login/login.html?logout=true") // Redirect on successful logout
                        .permitAll()
                )
                .csrf().disable() // Disable CSRF protection (disable for simplicity, consider enabling in production)
                // Configure session management (limit to 1 active session per user)
                .sessionManagement(session -> session
                        .maximumSessions(1) // Limit to one session per user
                        .expiredUrl("/login/login.html?sessionExpired=true") // Redirect when session expires
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername(username)
                        .password(passwordEncoder.encode(password)) // BCrypt hashed password
                        .roles("ADMIN")  // Assign "ADMIN" role to this user
                        .build()
        );
    }
    }
