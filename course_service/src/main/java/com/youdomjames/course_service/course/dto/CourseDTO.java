package com.youdomjames.course_service.course.dto;

import com.youdomjames.course_service.enumeration.Session;
import com.youdomjames.course_service.enumeration.Status;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CourseDTO implements Serializable {
    private String id;
    private String name;
    private String code;
    private String logoUrl;
    private BigDecimal price;
    private String teacherId;
    private Status status;
    private Session session;
    private String year;
}
