package com.youdomjames.teacher_service.service;

import com.youdomjames.teacher_service.domain.Teacher;
import com.youdomjames.teacher_service.dto.TeacherDTO;
import com.youdomjames.teacher_service.dto.mapper.MapstructMapper;
import com.youdomjames.teacher_service.exception.ApiException;
import com.youdomjames.teacher_service.forms.TeacherForm;
import com.youdomjames.teacher_service.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-17
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Slf4j
@Service
public record TeacherService(TeacherRepository repository, MapstructMapper mapper, SearchService searchService) {
    public TeacherDTO create(TeacherForm teacherForm) {
        if (repository.findByEmail(teacherForm.getEmail()).isPresent()) {
            throw new ApiException("Teacher already present.");
        }
        return mapper.toTeacherDTO(repository.save(mapper.toTeacher(teacherForm)));
    }

    public TeacherDTO getById(String id) {
        return mapper.toTeacherDTO(findById(id));
    }

    public TeacherDTO update(String id, TeacherForm teacherForm) {
        Teacher teacher = findById(id);
        return mapper.toTeacherDTO(repository.save(mapper.updateTeacher(teacherForm, teacher)));
    }

    private Teacher findById(String id) {
        return repository.findById(id).orElseThrow(() -> new ApiException("Teacher not found"));
    }

    public void deleteById(String id) {
        repository.deleteById(id);
        if (repository.existsById(id)) {
            throw new ApiException("Teacher not deleted");
        }
    }

    public Page<TeacherDTO> search(String searchText, String searchType, String operation, int pageNumber, int pageSize) {
        int pageIndex = pageNumber - 1;
        return repository.findAll(searchService.getSpecification(searchType, searchText, operation), PageRequest.of(pageIndex, pageSize)).map(mapper::toTeacherDTO);
    }

    public List<String> getTeacherCourses(String id) {
        return findById(id).getCourseIds();
    }


}
