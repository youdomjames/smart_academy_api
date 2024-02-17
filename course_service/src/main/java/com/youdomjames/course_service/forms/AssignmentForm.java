package com.youdomjames.course_service.forms;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentForm {
    @NotNull(message = "Please provide the assignment's url")
    private String fileUrl;
    private String comments;
    @NotNull(message = "Please provide the assignment's course")
    private String courseId;
    @NotNull(message = "Please provide the assignment's start date")
    @FutureOrPresent(message = "Please provide a date and time equal to or greater than now")
    private LocalDateTime startDate;
    @NotNull(message = "Please provide the assignment's deadline")
    @Future(message = "Please provide a date and time greater than now")
    private LocalDateTime deadline;
}
