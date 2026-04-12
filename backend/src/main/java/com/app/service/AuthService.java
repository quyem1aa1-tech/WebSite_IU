package com.app.service;

import com.app.dto.SignupRequest;
import com.app.entity.LoginStatus;
import com.app.entity.User;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginStatus loginUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        // 1. Check if user exists
        if (userOpt.isEmpty()) {
            return LoginStatus.USER_NOT_FOUND;
        }

        User user = userOpt.get();

        // 2. Check if password matches // !!REQUIRE ENCRYPTION!!
        if (!user.getPassword().equals(password)) {
            return LoginStatus.WRONG_PASSWORD;
        }

        // 3. Everything is correct
        return LoginStatus.SUCCESS;

    }

    // hàm yêu cầu
    public String registerUser(SignupRequest request) {

        // 1. Check if user exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "ERROR: Username already taken";
        }

        // 1.5 FIX: the user must write full name !
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            return "ERROR: Full Name is required and cannot be blank";
        }
        if (request.getRole() == null) {
            return "ERROR: Role is required! Please choose a role.";
        }

        // 2. Create new User entity
        User user = new User();
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setRole(request.getRole());

        // 3. SECURE THE PASSWORD (ĐÃ ĐƯỢC MÃ HÓA BẰNG BCRYPT)
        // Lấy mật khẩu gốc -> Đưa qua máy xay -> Lưu vào biến user
        String encodePassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodePassword);

        // 4. Save to Database (H2)
        userRepository.save(user);

        System.out.println("Tài khoản mới: " + request.getUsername() + " | Pass mã hóa: " + encodePassword);

        return "SUCCESS: User registered";
    }

    public boolean forgotPassword(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        // Email found
        if (user.isPresent()) {
            return true;
        }

        // Email not found
        return false;
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
