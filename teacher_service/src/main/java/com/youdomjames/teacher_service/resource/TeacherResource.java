package com.youdomjames.teacher_service.resource;

import com.youdomjames.teacher_service.dto.HttpResponse;
import com.youdomjames.teacher_service.dto.TeacherDTO;
import com.youdomjames.teacher_service.forms.AssignmentResultsForm;
import com.youdomjames.teacher_service.forms.TeacherForm;
import com.youdomjames.teacher_service.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static java.time.LocalTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-17
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@RestController
@RequestMapping("/teachers")
public record TeacherResource(TeacherService teacherService) {
    @PostMapping
    public ResponseEntity<HttpResponse> createTeacher(@RequestBody @Valid TeacherForm teacher) {
        TeacherDTO teacherDTO = teacherService.create(teacher);
        return ResponseEntity.status(CREATED).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Teacher created")
                        .data(of("teacher", teacherDTO))
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> getTeacherById(@PathVariable String id) {
        TeacherDTO teacherDTO = teacherService.getById(id);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Teacher fetched")
                        .data(of("teacher", teacherDTO))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/by-list-of-ids")
    public ResponseEntity<HttpResponse> getAllTeachersByListOfIds(@RequestParam Set<String> ids) {
        Set<TeacherDTO> teachers = teacherService.getAllByIds(ids);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Teachers fetched")
                        .data(of("teachers", teachers))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<HttpResponse> search(@RequestParam String searchText,
                                               @RequestParam String searchType,
                                               @RequestParam(required = false) String operation,
                                               @RequestParam int pageNumber,
                                               @RequestParam int pageSize) {
        Page<TeacherDTO> teachers = teacherService.search(searchText, searchType, operation, pageNumber, pageSize);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Teachers fetched")
                        .data(of("teachers", teachers))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpResponse> updateTeacher(@PathVariable String id, @RequestBody TeacherForm teacherForm) {
        TeacherDTO teacherDTO = teacherService.update(id, teacherForm);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Teacher updated")
                        .data(of("teacher", teacherDTO))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteTeacher(@PathVariable String id) {
        teacherService.deleteById(id);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Teacher Deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<HttpResponse> getTeacherCourses(@PathVariable String id) {
        Set<String> courses = teacherService.getTeacherCourses(id);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Teacher's courses fetched")
                        .data(of("courses", courses))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("{id}/courses/send/results")
    public ResponseEntity<HttpResponse> sendResults(@PathVariable String id, @RequestBody @Valid AssignmentResultsForm resultsForm) {
        teacherService.sendResults(id, resultsForm);
        return ResponseEntity.status(OK).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Results are being added/updated. A notification will be sent when completed.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

}
