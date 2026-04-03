package com.course.security.repository;

import com.course.security.models.Instructors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IInstructors extends JpaRepository<Instructors, Long> {
    Optional<Instructors> findByEmail(String email);
}
