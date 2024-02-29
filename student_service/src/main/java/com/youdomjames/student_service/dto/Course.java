package com.youdomjames.student_service.dto;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-28
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Getter
public class Course {
    private String id;
    private BigDecimal price;
}
