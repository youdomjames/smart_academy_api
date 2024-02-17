package com.youdomjames.teacher_service.enumeration;

import lombok.Getter;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-17
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Getter
public enum Default {
    PICTURE_URL("https://cdn-icons-png.flaticon.com/128/747/747376.png");
    private final String value;

    Default(String value) {
        this.value = value;
    }
}
