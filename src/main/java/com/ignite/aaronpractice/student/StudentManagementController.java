package com.ignite.aaronpractice.student;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("management/api/v1/students")
public class StudentManagementController {
    public static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "James Bond"),
            new Student(2, "Maria Jones"),
            new Student(3, "Anna Smith")
    );

    @GetMapping
    public List<Student> getAllStudents() {
        System.out.println("All students");
        return STUDENTS;
    }

    @PostMapping
    public void registerNewStudent(@RequestBody Student student) {
        System.out.println("Register A Student");
        System.out.println(student);
    }
    @DeleteMapping(path= "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Integer studentId) {
        System.out.println("DeleteStudent");
        System.out.println(studentId);
    }

    @PutMapping(path= "{studentId}")
    public void updateStudent(@PathVariable("studentId") Integer studentId , @RequestBody Student student) {
        System.out.printf("%s %s%n", studentId, student);
    }
}

