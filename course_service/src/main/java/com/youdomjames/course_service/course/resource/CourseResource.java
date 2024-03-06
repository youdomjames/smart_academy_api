package com.youdomjames.course_service.course.resource;

import com.youdomjames.course_service.HttpResponse;
import com.youdomjames.course_service.course.dto.CourseDTO;
import com.youdomjames.course_service.course.service.CourseService;
import com.youdomjames.course_service.forms.CourseForm;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

//TODO Implement authorizations
@RestController
@RequestMapping("/courses")
public record CourseResource(CourseService courseService) {
    @PostMapping
    public ResponseEntity<HttpResponse> addNewCourse(@RequestBody @Valid CourseForm course) {
        CourseDTO courseDTO = courseService.addCourse(course);
        return ResponseEntity.status(CREATED).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("course", courseDTO))
                        .message("Course created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> getCourseById(@PathVariable("id") String courseId) {
        CourseDTO courseDTO = courseService.getCourseDTOById(courseId);
        return ResponseEntity.status(OK).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("course", courseDTO))
                        .message("Course fetched")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/by-list-of-ids")
    public ResponseEntity<HttpResponse> getCoursesByIds(@RequestParam List<String> courseIds) {
        List<CourseDTO> courseDTOs = courseService.getAllCoursesByIds(courseIds);
        return ResponseEntity.status(OK).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("courses", courseDTOs))
                        .message("Courses fetched")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<HttpResponse> getCoursesBySearchParam(@RequestParam String searchText,
                                                                @RequestParam Integer page,
                                                                @RequestParam Integer size) {
        Page<CourseDTO> coursePage = courseService.getAllCoursesBySearchParam(searchText, page, size);
        return ResponseEntity.status(OK).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("page", coursePage))
                        .message("Courses fetched")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpResponse> updateCourseById(@PathVariable String id, @RequestBody CourseForm courseForm) {
        CourseDTO courseDTO = courseService.updateCourse(id, courseForm);
        return ResponseEntity.status(OK).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("course", courseDTO))
                        .message("Course updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteCourseById(@PathVariable String id) {
        courseService.deleteCourse(id);
        return ResponseEntity.status(OK).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Course deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
