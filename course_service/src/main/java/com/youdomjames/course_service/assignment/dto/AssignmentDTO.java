package com.youdomjames.course_service.assignment.dto;

import com.youdomjames.course_service.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDTO {
    private String id;
    private String code;
    private String title;
    private Status status;
    private String fileUrl;
    private String comments;
    private String courseId;
    private LocalDateTime startDate;
    private LocalDateTime deadline;
    private BigDecimal averageScore;
    private BigDecimal studentCompletionRate;
}
