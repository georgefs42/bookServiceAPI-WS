package com.george.books_service.services;

import com.george.books_service.models.AppUser;
import com.george.books_service.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<AppUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public AppUser saveUser(AppUser appUser) {
        return userRepository.save(appUser);
    }

    public void deleteUser(AppUser appUser) {
        userRepository.delete(appUser);
    }
}
