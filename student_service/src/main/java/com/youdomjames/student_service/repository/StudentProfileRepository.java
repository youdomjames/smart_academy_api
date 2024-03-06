package com.youdomjames.student_service.repository;

import com.youdomjames.student_service.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-22
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
public interface StudentProfileRepository extends JpaRepository<Profile, String>, JpaSpecificationExecutor<Profile> {
    Optional<Profile> findByEmail(String email);
}
