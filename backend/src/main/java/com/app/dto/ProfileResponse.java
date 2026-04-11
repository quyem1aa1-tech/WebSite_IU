package com.app.dto;

import com.app.entity.UserRole;

public class ProfileResponse {
    private String username;
    private String fullName;
    private String studentId;
    private String email;

    // Constructor
    public ProfileResponse(String studentId, String username, String fullName, String email) {
        this.studentId = studentId;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters and Setters (Important for JSON conversion)
    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getEmail() {
        return email;
    }
}
