package com.youdomjames.student_service.service.payment;

import com.youdomjames.student_service.domain.Student;
import com.youdomjames.student_service.dto.Course;
import com.youdomjames.student_service.dto.HttpResponse;
import com.youdomjames.student_service.exception.ApiException;
import com.youdomjames.student_service.service.StudentService;
import com.youdomjames.student_service.service.course.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-23
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Slf4j
@Service
public record PaymentServiceImpl(WebClient webClient, StudentService studentService,
                                 CourseService courseService) implements PaymentService {
    public static final String PORT = "9008";
    public static final String HOST = "localhost";
    private static final String PATH = "/payments/create";

    @Override
    public HttpResponse checkOut(String profileId, Set<String> courseIds) throws ApiException {
        if (!isSameProfileConnected(profileId)) {
            throw new ApiException("Unable to Checkout");
        }
        if (!isStudentRelatedToAllCoursesProvided(profileId, courseIds)) {
            throw new ApiException("Unable to Checkout");
        }
        Student student = getStudent(profileId);
        if (Objects.nonNull(student.getPendingPaymentIntentSecret())) {
            return HttpResponse.builder()
                    .timeStamp(now().toString())
                    .data(of("paymentIntentSecret", student.getPendingPaymentIntentSecret()))
                    .message("Pending checkout retrieved")
                    .status(CREATED)
                    .statusCode(CREATED.value())
                    .build();
        }
        HttpResponse response = webClient
                .method(HttpMethod.POST)
                .uri(uriBuilder -> uriBuilder.host(HOST)
                        .port(PORT)
                        .path(PATH)
                        .queryParam("amount", calculateTotalAmount(courseIds))
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(HttpResponse.class).block();
        assert response != null;
        if (response.getStatus().isError()) {
            log.error("Payment service returned error code {} with message {}", response.getStatusCode(), response.getMessage());
            log.debug(response.getDeveloperMessage());
            return response;
        }
        log.info("Stripe PaymentIntent created. Data = {}", response.getData());
        if (!response.getData().containsKey("paymentIntentSecret")) {
            log.error("PaymentIntent secret not present!");
            throw new ApiException("Unable to checkout");
        }
        String paymentIntentSecret = (String) response.getData().get("paymentIntentSecret");
        student.setPendingPaymentIntentSecret(paymentIntentSecret);
        studentService.updateStudent(student);
        return response;
    }

    @Override
    public HttpResponse handleSuccessfulPayment(String profileId, String paymentIntentSecret) throws ApiException {
        Student student = getStudent(profileId);
        if (!paymentIntentSecret.equals(student.getPendingPaymentIntentSecret())) {
            log.error("Illegal request with paymentIntentSecret= {}", paymentIntentSecret);
            throw new ApiException("Unable to handle successful payment");
        }
        student.setPendingPaymentIntentSecret(null);
        return HttpResponse.builder()
                .timeStamp(now().toString())
                .message("Payment successfully handled. Thank you!")
                .status(CREATED)
                .statusCode(CREATED.value())
                .build();
    }

    private boolean isStudentRelatedToAllCoursesProvided(String profileId, Set<String> courseIds) {
        Student student = getStudent(profileId);
        if (!student.getCourseIds().containsAll(courseIds) || !courseIds.containsAll(student.getCourseIds())) {
            log.error("Illegal Checkout request!");
            log.debug("There is at least one course not related to this student. Profile Id= {} courseIdsToPay= {}", profileId, courseIds);
            return false;
        }
        return true;
    }

    private Student getStudent(String profileId) {
        return studentService.getStudentByProfileId(profileId);
    }

    private BigDecimal calculateTotalAmount(Set<String> items) {
        HttpResponse response = courseService.getStudentCoursesById(items);
        Set<Course> courses;
        try {
            courses = (Set<Course>) response.getData().get("studentCourses");
            if (courses.stream().map(Course::getId).count() != items.size()) {
                log.error("Unable to fetch all courses");
                throw new ApiException("Unable to fetch all course prices");
            }
            return courses.stream().map(Course::getPrice).reduce(BigDecimal::add)
                    .orElseThrow(() -> new ApiException("Unable to calculate total amount"));
        } catch (ClassCastException | NullPointerException e) {
            log.error("Unable to cast studentCourses from response data");
            throw new ApiException("Unable to fetch course prices");
        }
    }

    private boolean isSameProfileConnected(String profileId) {
        log.debug("This request has not been issued by the related student only");
        return true; //TODO: Check id connected profile Id == profileId. If not, log error: Illegal request with the connected profile Id and profileId.
    }
}
