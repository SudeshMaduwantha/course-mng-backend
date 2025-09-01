package com.university.coursemanagement.controller;
import com.university.coursemanagement.entity.Registration;
import com.university.coursemanagement.entity.Student;
import com.university.coursemanagement.entity.Course;
import com.university.coursemanagement.repository.RegistrationRepository;
import com.university.coursemanagement.repository.StudentRepository;
import com.university.coursemanagement.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins ={"http://localhost:3000", "https://university-course-management-system.vercel.app"})
public class RegistrationController {
    
    @Autowired
    private RegistrationRepository registrationRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    // GET all registrations
    @GetMapping
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }
    
    // GET registrations by student ID
    @GetMapping("/student/{studentId}")
    public List<Registration> getRegistrationsByStudent(@PathVariable Long studentId) {
        return registrationRepository.findByStudentId(studentId);
    }
    
    // GET registrations by course ID
    @GetMapping("/course/{courseId}")
    public List<Registration> getRegistrationsByCourse(@PathVariable Long courseId) {
        return registrationRepository.findByCourseId(courseId);
    }
    
    // POST - Create new registration (enroll student in course)
    @PostMapping("/enroll")
    public ResponseEntity<Registration> enrollStudent(@RequestBody Map<String, Long> request) {
        try {
            Long studentId = request.get("studentId");
            Long courseId = request.get("courseId");
            
            Optional<Student> student = studentRepository.findById(studentId);
            Optional<Course> course = courseRepository.findById(courseId);
            
            if (student.isPresent() && course.isPresent()) {
                // Check if already enrolled
                Optional<Registration> existing = registrationRepository.findByStudentAndCourse(student.get(), course.get());
                if (existing.isPresent()) {
                    return ResponseEntity.badRequest().build(); // Already enrolled
                }
                
                Registration registration = new Registration(student.get(), course.get());
                Registration saved = registrationRepository.save(registration);
                return ResponseEntity.ok(saved);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT - Update grade
    @PutMapping("/{id}/grade")
    public ResponseEntity<Registration> updateGrade(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        try {
            Optional<Registration> optionalRegistration = registrationRepository.findById(id);
            if (optionalRegistration.isPresent()) {
                Registration registration = optionalRegistration.get();
                Double grade = request.get("grade");
                
                if (grade != null && grade >= 0 && grade <= 100) {
                    registration.setGrade(grade);
                    Registration saved = registrationRepository.save(registration);
                    return ResponseEntity.ok(saved);
                } else {
                    return ResponseEntity.badRequest().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // GET student transcript (all grades)
    @GetMapping("/transcript/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentTranscript(@PathVariable Long studentId) {
        try {
            Optional<Student> student = studentRepository.findById(studentId);
            if (student.isPresent()) {
                List<Registration> registrations = registrationRepository.findByStudentId(studentId);
                Double averageGrade = registrationRepository.calculateAverageGrade(studentId);
                
                Map<String, Object> transcript = new HashMap<>();
                transcript.put("student", student.get());
                transcript.put("registrations", registrations);
                transcript.put("gpa", averageGrade != null ? Math.round(averageGrade * 100.0) / 100.0 : 0.0);
                transcript.put("totalCourses", registrations.size());
                transcript.put("completedCourses", registrations.stream().filter(r -> r.getGrade() != null).count());
                
                return ResponseEntity.ok(transcript);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // GET course grade report
    @GetMapping("/course-report/{courseId}")
    public ResponseEntity<Map<String, Object>> getCourseGradeReport(@PathVariable Long courseId) {
        try {
            Optional<Course> course = courseRepository.findById(courseId);
            if (course.isPresent()) {
                List<Registration> registrations = registrationRepository.findByCourseId(courseId);
                
                double totalGrades = registrations.stream()
                    .filter(r -> r.getGrade() != null)
                    .mapToDouble(Registration::getGrade)
                    .sum();
                
                long gradedStudents = registrations.stream()
                    .filter(r -> r.getGrade() != null)
                    .count();
                
                double averageGrade = gradedStudents > 0 ? totalGrades / gradedStudents : 0.0;
                
                Map<String, Object> report = new HashMap<>();
                report.put("course", course.get());
                report.put("registrations", registrations);
                report.put("totalStudents", registrations.size());
                report.put("gradedStudents", gradedStudents);
                report.put("averageGrade", Math.round(averageGrade * 100.0) / 100.0);
                
                return ResponseEntity.ok(report);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // DELETE registration
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegistration(@PathVariable Long id) {
        if (registrationRepository.existsById(id)) {
            registrationRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}