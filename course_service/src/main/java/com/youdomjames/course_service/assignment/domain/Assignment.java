package com.youdomjames.course_service.assignment.domain;

import com.youdomjames.course_service.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Document
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {
    @Id
    private String id;
    private String code;
    private Status status;
    private String fileUrl;
    private String comments;
    private String courseId;
    private LocalDateTime startDate;
    private LocalDateTime deadline;
    private BigDecimal averageScore;
    private BigDecimal studentCompletionRate;
    @Builder.Default
    private Map<String, StudentAssignment> studentAssignments = new HashMap<>();
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
