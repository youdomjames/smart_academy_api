package com.youdomjames.course_service.course.resource;

import com.youdomjames.course_service.HttpResponse;
import com.youdomjames.course_service.course.domain.Student;
import com.youdomjames.course_service.course.service.StudentInCourseService;
import com.youdomjames.course_service.enumeration.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

//TODO Implement authorizations
@RestController
@RequestMapping("/courses/{id}/students")
public record StudentInCourseResource(StudentInCourseService studentService) {

    @PostMapping("/{studentId}")
    public ResponseEntity<HttpResponse> addStudentToCourse(@PathVariable("id") String courseId, @PathVariable String studentId) {
        Student student = studentService.addStudent(courseId, studentId);
        return ResponseEntity.status(CREATED).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("student", student))
                        .message("Student added to course")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<HttpResponse> getStudentsInCourse(@PathVariable("id") String courseId) {
        Map<String, Student> students = studentService.getStudentsInCourse(courseId);
        return ResponseEntity.status(OK).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("students", students))
                        .message("Students in Course fetched")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<HttpResponse> getStudentInCourseById(@PathVariable("id") String courseId, @PathVariable String studentId) {
        Student student = studentService.getStudentInCourse(courseId, studentId);
        return ResponseEntity.status(OK).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("student", student))
                        .message("Student in Course fetched")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PatchMapping("/{studentId}")
    public ResponseEntity<HttpResponse> updateStudentStatusInClass(@PathVariable("id") String courseId,
                                                                   @PathVariable String studentId,
                                                                   @RequestParam(required = false) Status statusInCourse,
                                                                   @RequestParam(required = false) Status coursePaymentStatus) {
        Student student = studentService.updateStudentStatusInCourse(courseId, studentId, statusInCourse, coursePaymentStatus);
        return ResponseEntity.status(OK).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("student", student))
                        .message(getMessage(statusInCourse, coursePaymentStatus))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<HttpResponse> deleteStudentFromCourse(@PathVariable("id") String courseId, @PathVariable String studentId) {
        studentService.deleteStudentFromCourse(courseId, studentId);
        return ResponseEntity.status(OK).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Student removed")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    private String getMessage(Status statusInCourse, Status coursePaymentStatus) {
        String message = "";
        String statusInCourseMessage = "Student status in course updated";
        String coursePaymentStatusMessage = "Student payment status updated";
        if (statusInCourse != null && coursePaymentStatus != null) {
            message = statusInCourseMessage.concat("\n").concat(coursePaymentStatusMessage);
        } else if (statusInCourse != null) {
            message = statusInCourseMessage;
        } else if (coursePaymentStatus != null) {
            message = coursePaymentStatusMessage;
        }
        return message;
    }
}
