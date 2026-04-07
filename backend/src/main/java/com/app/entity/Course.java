package com.app.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String courseName;

    @ManyToMany()
    @JoinTable(name = "course_student", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> liststudents = new HashSet<>();

    public Course() {
    }

    public Course(String courseName) {
        this.courseName = courseName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    // Đã đổi tên hàm cho chuẩn
    public Set<User> getliststudents() {
        return liststudents;
    }

    public void setliststudents(Set<User> liststudents) {
        this.liststudents = liststudents;
    }

    public int getStudentCount() {
        if (this.liststudents == null)
            return 0;
        return this.liststudents.size();
    }

    // Công cụ giúp học sinh
    public void addStudent(User user) {
        this.liststudents.add(user);
        user.getCourses().add(this);
    }

    public void removeStudent(User user) {
        this.liststudents.remove(user);
        user.getCourses().remove(this);
    }
}