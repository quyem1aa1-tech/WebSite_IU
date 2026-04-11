package com.app.service;

import com.app.entity.Course;
import com.app.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    /**
     * Truy vấn toàn bộ danh sách khóa học hiện có trong hệ thống.
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Tìm khóa học qua Id và tên.
     */
    public List<Course> searchCourses(String courseName, String courseId) {

        // Find by Id first
        if (courseId != null) return courseRepository.findByCourseIdContainingIgnoreCase(courseId);

        // Find by name
        if (courseName != null) return courseRepository.findByCourseNameContainingIgnoreCase(courseName);

        // Find all
        return getAllCourses();
    }
}
