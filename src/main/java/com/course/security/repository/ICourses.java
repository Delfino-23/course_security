package com.course.security.repository;

import com.course.security.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICourses extends JpaRepository<Course, Long> {
    Optional<Course> findByName(String name);
}
