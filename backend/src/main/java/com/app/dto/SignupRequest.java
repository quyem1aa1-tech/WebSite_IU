package com.app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import com.app.entity.UserRole;

public class SignupRequest {
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Full name is required")
    private String password;

    @NotBlank(message = "Password cannot be blank")
    private String fullName;

    @NotNull(message = "Role is required")
    private UserRole role; // e.g., "STUDENT" or "TEACHER"

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
