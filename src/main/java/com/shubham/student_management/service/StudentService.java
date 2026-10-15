package com.shubham.student_management.service;

import com.shubham.student_management.entity.Student;
import com.shubham.student_management.repository.StudentRepository;
import org.springframework.stereotype.Service;
import com.shubham.student_management.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    // Constructor injection: Spring supplies the repository automatically
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));

    }

    public Student updateStudent(Long id, Student updated) {
        Student existing = getStudentById(id);
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setBranch(updated.getBranch());
        existing.setYear(updated.getYear());
        return studentRepository.save(existing);
    }

    public void deleteStudent(Long id) {
        Student existing = getStudentById(id); // throws if it doesn't exist
        studentRepository.delete(existing);
    }
}