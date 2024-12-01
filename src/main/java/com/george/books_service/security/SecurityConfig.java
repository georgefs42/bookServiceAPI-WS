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

    // Security filter chain bean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Authorization rules
                .authorizeRequests(auth -> auth
                        .requestMatchers("/admin/admindashboard.html").hasRole("ADMIN")  // Only "ADMIN" can access
                        .anyRequest().permitAll()  // Allow all other requests
                )
                // Form login configuration
                .formLogin(form -> form
                        .loginPage("/login/login.html")  // Custom login page
                        .loginProcessingUrl("/login")  // Login processing URL
                        .defaultSuccessUrl("/admin/admindashboard.html", true)  // Redirect after successful login
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

    // User details service for in-memory authentication
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername(username)
                        .password(passwordEncoder.encode(password))  // Use encoded password
                        .roles("ADMIN")  // Assign "ADMIN" role
                        .build()
        );
    }
}
