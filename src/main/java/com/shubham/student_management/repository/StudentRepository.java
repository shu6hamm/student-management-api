package com.shubham.student_management.repository;

import com.shubham.student_management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Empty on purpose: JpaRepository gives us save(), findAll(), findById(), deleteById() ...
}