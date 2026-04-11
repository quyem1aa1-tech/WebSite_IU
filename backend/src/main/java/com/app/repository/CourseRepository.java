package com.app.repository;

import com.app.entity.Course;
import com.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Để trống ở đây cũng được, bạn đã có sẵn hàm save(), findById(),...
    Optional<Course> findByCourseId(String courseId);
}