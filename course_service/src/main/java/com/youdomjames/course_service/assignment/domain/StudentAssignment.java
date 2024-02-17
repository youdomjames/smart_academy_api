package com.youdomjames.course_service.assignment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAssignment {
    private BigDecimal score;
    private String grade;
    private String fileUrl;
    private LocalDateTime completionDateTime;
}
