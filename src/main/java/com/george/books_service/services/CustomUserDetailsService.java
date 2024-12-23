package com.george.books_service.services;

import com.george.books_service.models.AppUser;
import com.george.books_service.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = userRepository.findByUsername(username);

        if (appUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        AppUser user = appUser.get();
        return User.withUsername(user.getUsername())
                .password(user.getPassword()) // Password must be hashed
                .roles(user.getRoles().toArray(new String[0]))
                .build();
    }
}
