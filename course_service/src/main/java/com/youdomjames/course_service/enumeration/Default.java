package com.youdomjames.course_service.enumeration;

import lombok.Getter;

@Getter
public enum Default {
    COURSE_URL("https://cdn-icons-png.flaticon.com/128/864/864685.png");

    private final String value;

    Default(String value) {
        this.value = value;
    }

}
