package com.course.security.repository;

import com.course.security.models.Administrators;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAdministrators extends JpaRepository<Administrators, Long> {
    Optional<Administrators> findByEmail(String email);
}
