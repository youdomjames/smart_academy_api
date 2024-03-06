package com.youdomjames.teacher_service.exception;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-20
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
public class KafkaRecordHandlingException extends RuntimeException {
    public KafkaRecordHandlingException(String message) {
        super(message);
    }
}
