package com.youdomjames.course_service.scheduled_tasks;

import com.youdomjames.course_service.assignment.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
@RequiredArgsConstructor
public class ScheduledTasks {
    private final AssignmentService assignmentService;

    @Async
    @Scheduled(fixedRate = 3600000)//executed every hour
    public void changeAssignmentStatusByStartDateOrEndDate() {
        assignmentService.changeAssignmentsStatusPeriodically();
    }

    @Async
    @Scheduled(fixedRate = 18000000)//executed every 5 hours
    public void updateAssignmentCompletionRate() {
        assignmentService.updateAssignmentCompletionRates();
    }
}
