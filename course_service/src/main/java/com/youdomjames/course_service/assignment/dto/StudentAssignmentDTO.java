package com.youdomjames.course_service.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-03-04
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAssignmentDTO {
    private AssignmentDTO assignment;
    private BigDecimal score;
    private String grade;
    private String fileUrl;
    private LocalDateTime completionDateTime;
}
