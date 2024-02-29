package com.youdomjames.student_service.service.course;

import com.youdomjames.student_service.dto.HttpResponse;
import com.youdomjames.student_service.exception.ApiException;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-27
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Service
public interface CourseService {
    HttpResponse getStudentCoursesById(@NotNull Set<String> courseIds) throws ApiException;
}
