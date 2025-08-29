package com.university.coursemanagement.controller;

import com.university.coursemanagement.entity.Student;
import com.university.coursemanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {
    
    @Autowired
    private StudentRepository studentRepository;
    
    // GET all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    // GET student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // GET student by student ID
    @GetMapping("/student-id/{studentId}")
    public ResponseEntity<Student> getStudentByStudentId(@PathVariable String studentId) {
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // POST - Create new student
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        try {
            Student savedStudent = studentRepository.save(student);
            return ResponseEntity.ok(savedStudent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT - Update existing student
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setFirstName(studentDetails.getFirstName());
            student.setLastName(studentDetails.getLastName());
            student.setEmail(studentDetails.getEmail());
            student.setPhoneNumber(studentDetails.getPhoneNumber());
            student.setDateOfBirth(studentDetails.getDateOfBirth());
            student.setProgram(studentDetails.getProgram());
            student.setYear(studentDetails.getYear());
            
            return ResponseEntity.ok(studentRepository.save(student));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE student
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}