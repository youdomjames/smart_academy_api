package com.youdomjames.teacher_service.exception.handler;

import com.youdomjames.teacher_service.dto.HttpResponse;
import com.youdomjames.teacher_service.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-17
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler({ApiException.class})
    public ResponseEntity<Object> handleApiException(
            Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message(ex.getMessage())
                        .status(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value())
                        .build()
        );
    }

    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity<Object> handleDuplicateKeyException() {
        return ResponseEntity.status(CONFLICT).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("This course is already present.")
                        .developerMessage("Try adding new one with different name, year or session")
                        .status(CONFLICT)
                        .statusCode(CONFLICT.value())
                        .build()
        );
    }
}
