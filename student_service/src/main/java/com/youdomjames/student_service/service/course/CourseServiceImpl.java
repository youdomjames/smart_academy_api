package com.youdomjames.student_service.service.course;

import com.youdomjames.student_service.domain.Student;
import com.youdomjames.student_service.dto.HttpResponse;
import com.youdomjames.student_service.exception.ApiException;
import com.youdomjames.student_service.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-27
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Slf4j
@Service
public record CourseServiceImpl(WebClient webClient, StudentService studentService) implements CourseService {
    private static final String PATH = "/courses/student-courses";
    private static final String PORT = "9005";
    private static final String HOST = "localhost";

    @Override
    public HttpResponse getStudentCoursesById(Set<String> courseIds) throws ApiException {
        HttpResponse response = webClient
                .method(HttpMethod.POST)
                .uri(uriBuilder -> uriBuilder.host(HOST)
                        .port(PORT)
                        .path(PATH)
                        .queryParam("courseIds", courseIds)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(HttpResponse.class).block();
        assert response != null;
        if (response.getStatus().isError()) {
            log.error("Course service returned error code {} with message {}", response.getStatusCode(), response.getMessage());
            log.debug(response.getDeveloperMessage());
            return response;
        }
        log.info("Student courses fetched");
        if (!response.getData().containsKey("studentCourses")) {
            log.error("Student courses not present!");
            throw new ApiException("Unable to fetch course prices");
        }
        return response;
    }

    @Override
    public void addCourse(String record) {
        String[] data = record.split(",");
        if (data.length != 2) {
            log.error("Invalid record");
            log.debug("Unable to add course, coming from Kafka. Invalid record");
        }
        String profileId = data[0];
        String courseId = data[1];

        Student student = studentService.getStudentByProfileId(profileId);
        if (student.getCourseIds().contains(courseId)) {
            log.error("Course already present");
            log.debug("Unable to add course, coming from Kafka. Course already present");
            //TODO: Notify admin that course is already present
        }
        student.getCourseIds().add(courseId);
        studentService.save(student);
        log.info("Course {} added", courseId);
        //TODO: Notify admin that course has been added
    }

    @Override
    public void removeCourse(String record) {
        String[] data = record.split(",");
        if (data.length != 2) {
            log.error("Invalid record");
            log.debug("Unable to remove course, coming from Kafka. Invalid record");
        }
        String profileId = data[0];
        String courseId = data[1];
        Student student = studentService.getStudentByProfileId(profileId);
        if (!student.getCourseIds().contains(courseId)) {
            log.error("Course not present");
            log.debug("Unable to remove course, coming from Kafka. Course not present");
            //TODO: Notify admin that course is not present
        }
        student.getCourseIds().remove(courseId);
        studentService.save(student);
        log.info("Course {} removed", courseId);
        //TODO: Notify admin that course has been removed
    }
}
