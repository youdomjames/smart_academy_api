package com.youdomjames.teacher_service.forms;

import com.youdomjames.teacher_service.enumeration.Gender;
import com.youdomjames.teacher_service.enumeration.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-17
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Data
public class TeacherForm {
    @NotNull(message = "Please provide a first name")
    @NotEmpty(message = "First name incorrect")
    @Size(min = 3, message = "First name should be at least 3 characters")
    private String firstName;
    @NotNull(message = "Please provide a last name")
    @NotEmpty(message = "Last name incorrect")
    @Size(min = 3, message = "Last name should be at least 3 characters")
    private String lastName;
    @NotNull(message = "Please provide a date of birth")
    private LocalDate dateOfBirth;
    @NotNull(message = "Please provide the teacher's hired date")
    private LocalDate hiredDate;
    private Status status;
    @Email(message = "Email address incorrect")
    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    private String phoneNumber;
    private Gender gender;
    private String aboutMe;
    private String highestDegree;
    @NotNull(message = "Please provide a salary")
    private BigDecimal salary;
    private String profilePictureLink;
    private AddressForm address;
}
