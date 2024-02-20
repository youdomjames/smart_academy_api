package com.youdomjames.teacher_service.forms;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-18
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Getter
@Setter
public class AssignmentResultsForm {
    @NotNull
    @NotEmpty
    private String courseId;
    @NotNull
    @NotEmpty
    private String assignmentId;
    @NotNull
    @NotEmpty
    private Map<String, BigDecimal> studentScores;
}
