package com.youdomjames.teacher_service.query;

import com.youdomjames.teacher_service.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-17
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Data
@AllArgsConstructor
public class SearchCriteria {
    private String type;
    private String operation;
    private String key;
    private Object value;

    public SearchCriteria(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public SearchCriteria(String searchType, String key, Object value) {
        this.type = searchType;
        this.key = key;
        this.value = value;
    }
}
