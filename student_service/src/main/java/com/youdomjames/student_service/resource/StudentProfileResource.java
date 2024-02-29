package com.youdomjames.student_service.resource;

import com.youdomjames.student_service.dto.HttpResponse;
import com.youdomjames.student_service.dto.ProfileDTO;
import com.youdomjames.student_service.form.ProfileForm;
import com.youdomjames.student_service.service.StudentProfileService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-22
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@RestController
@RequestMapping("/profiles")
public record StudentProfileResource(StudentProfileService service) {
    //TODO: Restrict access to ADMIN
    @PostMapping
    public ResponseEntity<HttpResponse> createStudent(@RequestBody @Valid ProfileForm profileForm) {
        ProfileDTO studentProfile = service.createStudent(profileForm);
        return ResponseEntity.status(CREATED).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("studentProfile", studentProfile))
                        .message("Student created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    //TODO: Restrict access to ADMIN,TEACHER,CONNECTED_STUDENT
    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> getStudentProfileById(@PathVariable String id) {
        ProfileDTO studentProfile = service.getStudentProfileById(id);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("studentProfile", studentProfile))
                        .message("Student created")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    //TODO: Restrict access to ADMIN,TEACHER
    @GetMapping
    public ResponseEntity<HttpResponse> searchStudent(@RequestParam String searchText,
                                                      @RequestParam int pageNumber,
                                                      @RequestParam String searchType,
                                                      @RequestParam(required = false) String operation,
                                                      @RequestParam int pageSize) {
        Page<ProfileDTO> studentProfiles = service.searchStudentProfiles(searchText, searchType, operation, pageNumber, pageSize);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("studentProfiles", studentProfiles))
                        .message("Profiles fetched")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/by-list-of-ids")
    public ResponseEntity<HttpResponse> getStudentProfilesByIds(@RequestParam Set<String> ids) {
        Set<ProfileDTO> studentProfiles = service.getStudentProfilesByIds(ids);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("studentProfiles", studentProfiles))
                        .message("Profiles fetched")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    //TODO: Restrict access to ADMIN,CONNECTED_STUDENT
    @PatchMapping("/{id}")
    public ResponseEntity<HttpResponse> updateStudentProfileById(@PathVariable String id, @RequestBody ProfileForm form) {
        ProfileDTO studentProfile = service.updateStudentProfileById(id, form);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("studentProfile", studentProfile))
                        .message("Student updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    //TODO: Restrict access to ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteStudentByProfileId(@PathVariable String id) {
        service.deleteStudentByProfileId(id);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Student deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
