package com.youdomjames.course_service.course.domain;

import com.youdomjames.course_service.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Status statusInCourse;
    private Status coursePaymentStatus;
    private BigDecimal assignmentsAverageScore;
    private BigDecimal examsAverageScore;
    private BigDecimal finalScore;
    private Character finalGrade;
}
