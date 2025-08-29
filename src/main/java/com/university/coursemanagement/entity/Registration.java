package com.university.coursemanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @Column(name = "registration_date", nullable = false)
    private String registrationDate;
    
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;
    
    private Double grade; // Final grade (0.0 - 100.0)
    
    @Enumerated(EnumType.STRING)
    @Column(name = "grade_status")
    private GradeStatus gradeStatus;
    
    // Constructors
    public Registration() {
        this.registrationDate = LocalDateTime.now().toString();
        this.status = RegistrationStatus.ENROLLED;
        this.gradeStatus = GradeStatus.NOT_GRADED;
    }
    
    public Registration(Student student, Course course) {
        this();
        this.student = student;
        this.course = course;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    
    public String getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }
    
    public RegistrationStatus getStatus() { return status; }
    public void setStatus(RegistrationStatus status) { this.status = status; }
    
    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { 
        this.grade = grade;
        if (grade != null) {
            this.gradeStatus = GradeStatus.GRADED;
        }
    }
    
    public GradeStatus getGradeStatus() { return gradeStatus; }
    public void setGradeStatus(GradeStatus gradeStatus) { this.gradeStatus = gradeStatus; }
    
    public String getLetterGrade() {
        if (grade == null) return "N/A";
        if (grade >= 90) return "A";
        if (grade >= 80) return "B";
        if (grade >= 70) return "C";
        if (grade >= 60) return "D";
        return "F";
    }
    
    public double getGradePoints() {
        if (grade == null) return 0.0;
        if (grade >= 90) return 4.0;
        if (grade >= 80) return 3.0;
        if (grade >= 70) return 2.0;
        if (grade >= 60) return 1.0;
        return 0.0;
    }
}

enum RegistrationStatus {
    ENROLLED, COMPLETED, DROPPED, WITHDRAWN
}

enum GradeStatus {
    NOT_GRADED, GRADED, PENDING_REVIEW
}