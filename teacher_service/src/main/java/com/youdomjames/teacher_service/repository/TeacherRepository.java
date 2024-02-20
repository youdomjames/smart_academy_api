package com.youdomjames.teacher_service.repository;

import com.youdomjames.teacher_service.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-17
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String>, JpaSpecificationExecutor<Teacher> {
    Optional<Teacher> findByEmail(String email);
//    @Query("SELECT '*' FROM Teacher t WHERE t.firstName LIKE :filter OR " +
//            "t.lastName LIKE :filer OR t.email LIKE :filer OR " +
//            "t.phoneNumber LIKE :filer OR t.aboutMe LIKE :filer OR t.highestDegree LIKE :filer OR " +
//            "t.address.apartmentNumber LIKE :filer OR t.address.city LIKE :filer OR t.address.state LIKE :filer OR t.address.street LIKE :filer OR " +
//            "t.address.postalCode LIKE :filer")
//    Page<Teacher> filterBy(@Param("filter") String filter, Pageable pageable);
}
