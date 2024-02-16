package com.youdomjames.course_service.assignment.resource;

import com.youdomjames.course_service.HttpResponse;
import com.youdomjames.course_service.assignment.domain.StudentAssignment;
import com.youdomjames.course_service.assignment.service.StudentAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

//TODO Implement authorizations
@RestController
@RequestMapping("/assignments/{id}/students")
public record StudentAssignmentResource(StudentAssignmentService studentAssignmentService) {
    @PostMapping("/{studentId}")
    public ResponseEntity<HttpResponse> addStudentAssigment(@PathVariable String id, @PathVariable String studentId,
                                                            @RequestParam String fileUrl) {
        StudentAssignment studentAssignment = studentAssignmentService.addStudentAssignment(id, studentId, fileUrl);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("studentAssignment", studentAssignment))
                        .message("Student Assignment added")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<HttpResponse> getStudentAssignment(@PathVariable String id, @PathVariable String studentId) {
        StudentAssignment studentAssignment = studentAssignmentService.getStudentAssignment(id, studentId);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("studentAssignment", studentAssignment))
                        .message("Student Assignment fetched")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PatchMapping("/{studentId}")
    public ResponseEntity<HttpResponse> updateStudentUrlFile(@PathVariable String id, @PathVariable String studentId,
                                                             @RequestParam String fileUrl) {
        StudentAssignment studentAssignment = studentAssignmentService.updateStudentUrlFile(id, studentId, fileUrl);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("studentAssignment", studentAssignment))
                        .message("Student Assignment file updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<HttpResponse> deleteStudentAssignment(@PathVariable String id, @PathVariable String studentId) {
        studentAssignmentService.deleteStudentAssignment(id, studentId);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Student Assignment deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<HttpResponse> getStudentAssignments(@PathVariable String id) {
        Map<String, StudentAssignment> studentAssignments = studentAssignmentService.getStudentAssignments(id);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("studentAssignments", studentAssignments))
                        .message("Student Assignments fetched")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
