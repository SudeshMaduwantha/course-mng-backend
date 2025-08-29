package com.university.coursemanagement.repository;

import com.university.coursemanagement.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Custom query methods
    Optional<Course> findByCode(String code);
    List<Course> findByTitleContainingIgnoreCase(String title);
}