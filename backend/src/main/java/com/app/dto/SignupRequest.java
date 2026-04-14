package com.app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.app.entity.UserRole;

public class SignupRequest {

    @NotBlank(message = "Student id cannot be blank")
    private String studentId;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Full name is required")
    private String password;

    @NotBlank(message = "Password cannot be blank")
    private String fullName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

}
