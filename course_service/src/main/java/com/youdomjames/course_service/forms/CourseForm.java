package com.youdomjames.course_service.forms;

import com.youdomjames.course_service.enumeration.Session;
import com.youdomjames.course_service.enumeration.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseForm {
    @NotEmpty(message = "Course's name cannot be empty")
    private String name;
    private String logoUrl;
    @NotNull(message = "Course's price cannot be null")
    private BigDecimal price;
    private String teacherId;
    private Status status;
    @NotNull(message = "Course's session cannot be null")
    private Session session;
    @Positive(message = "Course's year should be superior to 0")
    private String year;
}
