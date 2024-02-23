package com.youdomjames.student_service.form;

import com.youdomjames.student_service.enumerations.Gender;
import com.youdomjames.student_service.enumerations.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
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
public class ProfileForm {
    @NotNull(message = "Please provide a First Name")
    @NotEmpty(message = "Please provide a non-empty First Name")
    @Size(min = 3, message = "First Name should be 3 characters or more")
    private String firstName;
    @NotNull(message = "Please provide a Last Name")
    @NotEmpty(message = "Please provide a non-empty Last Name")
    @Size(min = 3, message = "Last Name should be 3 characters or more")
    private String lastName;
    @NotNull(message = "Please provide a Date of Birth")
    @Past(message = "The date of birth provided is not valid")
    private LocalDate dateOfBirth;
    private Status status;
    private String email;
    @NotNull(message = "Please provide a phone number")
    private String phoneNumber;
    @NotNull(message = "Please provide a gender")
    private Gender gender;
    private String aboutMe;
    private String profilePictureLink;
    @NotNull(message = "Please provide an address")
    private AddressForm address;
}
