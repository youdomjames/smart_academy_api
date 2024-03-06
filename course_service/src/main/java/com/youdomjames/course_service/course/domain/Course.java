package com.youdomjames.course_service.course.domain;

import com.youdomjames.course_service.enumeration.Session;
import com.youdomjames.course_service.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String code;
    private String logoUrl;
    private BigDecimal price;
    private String teacherId;
    private Status status;
    @Builder.Default
    private Map<String, Student> students = new HashMap<>();
    private Session session;
    private String year;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
