package com.youdomjames.student_service.repository;

import com.youdomjames.student_service.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-22
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByProfileId(String profileId);
}
