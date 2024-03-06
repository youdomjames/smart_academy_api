package com.youdomjames.teacher_service.exception;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-17
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
