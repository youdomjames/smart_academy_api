package com.youdomjames.student_service.service.course;

import com.youdomjames.student_service.dto.HttpResponse;
import com.youdomjames.student_service.exception.ApiException;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-27
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
public interface CourseService {
    HttpResponse getStudentCoursesById(@NotNull Set<String> courseIds) throws ApiException;

    void addCourse(String record);

    void removeCourse(String record);
}
