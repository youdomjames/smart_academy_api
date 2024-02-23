package com.youdomjames.student_service.domain;

import com.youdomjames.student_service.enumerations.Gender;
import com.youdomjames.student_service.enumerations.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-22
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @Column(unique = true)
    private String email;
    private String phoneNumber;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @Column(columnDefinition = "TEXT")
    private String aboutMe;
    private String profilePictureLink;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    private Student student;
}
