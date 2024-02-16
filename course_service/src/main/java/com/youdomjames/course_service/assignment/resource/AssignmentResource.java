package com.youdomjames.course_service.assignment.resource;

import com.youdomjames.course_service.HttpResponse;
import com.youdomjames.course_service.assignment.dto.AssignmentDTO;
import com.youdomjames.course_service.assignment.service.AssignmentService;
import com.youdomjames.course_service.forms.AssignmentForm;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

//TODO Implement authorizations
@RestController
@RequestMapping("/assignments")
public record AssignmentResource(AssignmentService assignmentService) {

    @PostMapping
    public ResponseEntity<HttpResponse> createAssignment(@RequestBody @Valid AssignmentForm assignmentForm) {
        AssignmentDTO assignmentDTO = assignmentService.createAssignment(assignmentForm);
        return ResponseEntity.status(CREATED).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("assignment", assignmentDTO))
                        .message("Assignment created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> getAssignmentById(@PathVariable String id) {
        AssignmentDTO assignmentDTO = assignmentService.getAssignmentDTOById(id);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("assignment", assignmentDTO))
                        .message("Assignment fetched")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<HttpResponse> getAssignments(@RequestParam String searchText,
                                                       @RequestParam(required = false) int pageNumber,
                                                       @RequestParam(required = false) int pageSize) {
        Page<AssignmentDTO> assignments = assignmentService.getAssignments(searchText, pageNumber, pageSize);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("assignments", assignments))
                        .message("Assignments fetched")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpResponse> updateAssignmentById(@PathVariable String id, @RequestBody AssignmentForm assignmentForm) {
        AssignmentDTO assignmentDTO = assignmentService.updateAssignmentById(id, assignmentForm);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("assignment", assignmentDTO))
                        .message("Assignment fetched")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteAssignmentById(@PathVariable String id) {
        assignmentService.deleteAssignmentById(id);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Assignment deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

}
