package com.app.service;

import com.app.entity.LoginStatus;
import com.app.entity.User;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public LoginStatus login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        // 1. Check if user exists
        if (userOpt.isEmpty()) {
            return LoginStatus.USER_NOT_FOUND;
        }

        User user = userOpt.get();

        // 2. Check if password matches
        if (!user.getPassword().equals(password)) {
            return LoginStatus.WRONG_PASSWORD;
        }

        // 3. Everything is correct
        return LoginStatus.SUCCESS;

        
    }

    
}
