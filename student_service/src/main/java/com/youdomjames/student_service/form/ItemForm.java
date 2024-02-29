package com.youdomjames.student_service.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-23
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Data
@Builder
public class ItemForm {
    @NotNull(message = "Provide a valid courseId")
    @NotEmpty
    private String courseId;
    @NotNull(message = "Provide a valid amount")
    @NotEmpty
    @Positive
    private BigDecimal amount;
}
