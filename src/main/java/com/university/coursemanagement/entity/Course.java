package com.university.coursemanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, unique = true)
    private String code;
    
    @Column(length = 1000)
    private String description;
    
    private Integer credits;
    
    // Default constructor (required by JPA)
    public Course() {}
    
    // Constructor with parameters
    public Course(String title, String code, String description, Integer credits) {
        this.title = title;
        this.code = code;
        this.description = description;
        this.credits = credits;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
}