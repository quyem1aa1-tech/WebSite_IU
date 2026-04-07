package com.app.repository;

import com.app.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Để trống ở đây cũng được, bạn đã có sẵn hàm save(), findById(),...
}