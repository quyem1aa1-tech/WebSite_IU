package com.app.service;

import com.app.entity.User;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all users from the database.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Find a specific user by their username.
     * Useful for login and profile viewing.
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Simple Authentication Logic
     * Returns true if the credentials are correct.
     */
    public boolean authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // In a real-world app, you should use password encoders (like BCrypt)
            return user.getPassword().equals(password);
        }
        return false;
    }

    /**
     * Create a new user (Student or Teacher)
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}