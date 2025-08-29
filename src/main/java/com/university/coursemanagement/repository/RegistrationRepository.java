package com.university.coursemanagement.repository;

import com.university.coursemanagement.entity.Registration;
import com.university.coursemanagement.entity.Student;
import com.university.coursemanagement.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByStudent(Student student);
    List<Registration> findByCourse(Course course);
    List<Registration> findByStudentId(Long studentId);
    List<Registration> findByCourseId(Long courseId);
    Optional<Registration> findByStudentAndCourse(Student student, Course course);
    
    // Find all registrations with grades
    List<Registration> findByGradeIsNotNull();
    
    // Find students by course for grading
    @Query("SELECT r FROM Registration r WHERE r.course.id = ?1")
    List<Registration> findRegistrationsByCourseId(Long courseId);
    
    // Calculate GPA for a student
    @Query("SELECT AVG(r.grade) FROM Registration r WHERE r.student.id = ?1 AND r.grade IS NOT NULL")
    Double calculateAverageGrade(Long studentId);
}