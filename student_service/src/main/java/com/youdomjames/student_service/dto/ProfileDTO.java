package com.youdomjames.student_service.dto;

import com.youdomjames.student_service.enumerations.Gender;
import com.youdomjames.student_service.enumerations.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-22
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Data
@Builder
public class ProfileDTO {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Status status;
    private String email;
    private String phoneNumber;
    private Gender gender;
    private String aboutMe;
    private String profilePictureLink;
    private AddressDTO address;
}
