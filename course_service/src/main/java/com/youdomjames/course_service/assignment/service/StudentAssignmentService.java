package com.youdomjames.course_service.assignment.service;

import com.youdomjames.course_service.assignment.domain.Assignment;
import com.youdomjames.course_service.assignment.domain.StudentAssignment;
import com.youdomjames.course_service.enumeration.Status;
import com.youdomjames.course_service.exception.ApiException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
public record StudentAssignmentService(AssignmentService assignmentService) {
    public StudentAssignment addStudentAssignment(String id, String studentId, String fileUrl) {
        Assignment assignment = assignmentService.getAssignmentById(id);
        studentAssignmentChecks(assignment, studentId);
        StudentAssignment studentAssignment = StudentAssignment.builder()
                .fileUrl(fileUrl)
                .completionDateTime(now())
                .build();
        assignment.getStudentAssignments().put(studentId, studentAssignment);
        assignmentService.save(assignment);
        assignment.setModifiedAt(now());
        return studentAssignment;
    }

    public StudentAssignment getStudentAssignment(Assignment assignment, String studentId) {
        StudentAssignment studentAssignment = assignment.getStudentAssignments().get(studentId);
        if (studentAssignment == null) {
            throw new ApiException("Student's assignment not found");
        }
        return studentAssignment;
    }

    public StudentAssignment getStudentAssignment(String assignmentId, String studentId) {
        return getStudentAssignment(assignmentService.getAssignmentById(assignmentId), studentId);
    }

    //TODO: Use kafka to get scores from teacher service
    public void addStudentScore(@NotNull @Positive BigDecimal score, @NotNull String assignmentId, @NotNull String studentId) {
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        assignment.getStudentAssignments().computeIfPresent(studentId, (key, value) -> {
            value.setScore(score);
            value.setGrade(getGrade(score.doubleValue()));
            return value;
        });
        assignment.setModifiedAt(now());
        assignmentService.save(assignment);
    }

    //TODO: Call this method after adding all student scores for an assignment
    public void setAverageScore(Assignment assignment) {
        BigDecimal sumOfScores = assignment.getStudentAssignments().values().stream()
                .map(StudentAssignment::getScore).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal averageScore = sumOfScores.divide(BigDecimal.valueOf(assignment.getStudentAssignments().size()), RoundingMode.HALF_UP);
        assignment.setAverageScore(averageScore);
        assignment.setModifiedAt(now());
        assignmentService.save(assignment);
    }

    public Map<String, StudentAssignment> getStudentAssignments(String id) {
        return assignmentService.getAssignmentById(id).getStudentAssignments();
    }

    public StudentAssignment updateStudentUrlFile(String id, String studentId, String fileUrl) {
        Assignment assignment = assignmentService.getAssignmentById(id);
        assignment.getStudentAssignments().computeIfPresent(studentId, (key, value) -> {
            value.setFileUrl(fileUrl);
            value.setCompletionDateTime(now());
            return value;
        });
        assignment.setModifiedAt(now());
        return assignmentService.save(assignment).getStudentAssignments().get(studentId);
    }

    public void deleteStudentAssignment(String id, String studentId) {
        StudentAssignment deletedStudentAssignment = assignmentService.getAssignmentById(id).getStudentAssignments().remove(studentId);
        if (deletedStudentAssignment == null) {
            throw new ApiException("Student Assignment not deleted");
        }
        log.info("Student assignment {} deleted. Assignment code = {}", deletedStudentAssignment, assignmentService.getAssignmentById(id).getCode());
    }

    private String getGrade(double score) {
        String grade;
        if (score >= 97.00 && score <= 100) {
            grade = "A+";
        } else if (score >= 93.00 && score < 97.00) {
            grade = "A";
        } else if (score >= 90.00 && score < 93.00) {
            grade = "A-";
        } else if (score >= 87.00 && score < 90.00) {
            grade = "B+";
        } else if (score >= 83.00 && score < 87.00) {
            grade = "B";
        } else if (score >= 80.00 && score < 83.00) {
            grade = "B-";
        } else if (score >= 77.00 && score < 80.00) {
            grade = "C+";
        } else if (score >= 73.00 && score < 77.00) {
            grade = "C";
        } else if (score >= 70.00 && score < 73.00) {
            grade = "C-";
        } else if (score >= 67.00 && score < 70.00) {
            grade = "D+";
        } else if (score >= 63.00 && score < 67.00) {
            grade = "D";
        } else if (score >= 60.00 && score < 63.00) {
            grade = "D-";
        } else
            grade = "F";
        return grade;
    }

    private void studentAssignmentChecks(Assignment assignment, String studentId) {
        if (assignment.getStartDate().isAfter(now())) {
            throw new ApiException("Assignment has not started yet.");
        }
        if (assignment.getDeadline().isBefore(now())) {
            throw new ApiException("Assignment deadline reached");
        }
        if (!assignment.getStatus().equals(Status.IN_PROGRESS)) {
            throw new ApiException("It is not possible to add upload your file to this assignment. Please contact administration/teacher");
        }
        if (assignment.getStudentAssignments().entrySet().stream().parallel()
                .anyMatch(entry -> entry.getKey().equals(studentId) && entry.getValue().getFileUrl() != null)) {
            throw new ApiException("Student assignment already uploaded");
        }
    }
}
