package com.youdomjames.student_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

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
public class StudentDTO {
    private String id;
    private ProfileDTO profileDTO;
    private Set<String> courseIds;
    private String pendingPaymentIntentSecret;
}
