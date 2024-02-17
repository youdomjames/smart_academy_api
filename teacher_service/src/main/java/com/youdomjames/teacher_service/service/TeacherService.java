package com.youdomjames.teacher_service.service;

import com.youdomjames.teacher_service.domain.Teacher;
import com.youdomjames.teacher_service.dto.TeacherDTO;
import com.youdomjames.teacher_service.dto.mapper.MapStructMapper;
import com.youdomjames.teacher_service.enumeration.Gender;
import com.youdomjames.teacher_service.enumeration.Status;
import com.youdomjames.teacher_service.exception.ApiException;
import com.youdomjames.teacher_service.forms.TeacherForm;
import com.youdomjames.teacher_service.query.SearchCriteria;
import com.youdomjames.teacher_service.query.TeacherSpecification;
import com.youdomjames.teacher_service.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
public record TeacherService(TeacherRepository repository, MapStructMapper mapper) {
    public TeacherDTO create(TeacherForm teacherForm) {
        if (repository.findByEmail(teacherForm.getEmail()).isPresent()) {
            throw new ApiException("Teacher already present.");
        }
        return mapper.teachertoTeacherDTO(repository.save(mapper.toTeacher(teacherForm)));
    }

    public TeacherDTO getById(String id) {
        return mapper.teachertoTeacherDTO(findById(id));
    }

    public TeacherDTO update(String id, TeacherForm teacherForm) {
        Teacher teacher = findById(id);
        return mapper.teachertoTeacherDTO(repository.save(mapper.updateTeacher(teacher, teacherForm)));
    }

    private Teacher findById(String id){
        return repository.findById(id).orElseThrow(()-> new ApiException("Teacher not found"));
    }

    public void deleteById(String id) {
        repository.deleteById(id);
        if (repository.existsById(id)){
            throw new ApiException("Teacher not deleted");
        }
    }

    public Page<TeacherDTO> search(String searchText, String searchType, int pageNumber, int pageSize) {
        int pageIndex = pageNumber - 1;
        return repository.findAll(getSpecification(searchType, searchText), PageRequest.of(pageIndex, pageSize)).map(mapper::teachertoTeacherDTO);
    }

    private Specification<Teacher> getSpecification(String searchType, String searchText){
        return switch (searchType) {
            case "STRING" -> getStringSpec(searchText);
            case "NUMBER" -> getSalarySpec(searchText);
            case "DATE" -> getDateSpec(searchText);
            case "ENUM" -> getEnumSpec(searchText);
            default -> throw new ApiException("Wrong search type. Please try again");
        };
    }

    private Specification<Teacher> getEnumSpec(String searchText) {
        try{
            Status status = Status.valueOf(searchText);
            return Specification.where(new TeacherSpecification(new SearchCriteria("status", status.toString())));
        } catch (IllegalArgumentException e){
            Gender gender = Gender.valueOf(searchText);
            return Specification.where(new TeacherSpecification(new SearchCriteria("gender", gender.toString())));
        } catch (Exception e){
            log.error(e.getMessage());
            throw new ApiException("Wrong search type. Please select ENUM type");
        }
    }

    private Specification<Teacher> getDateSpec(String searchText) {
        try{
            LocalDate date = LocalDate.parse(searchText);
            return Specification.where(new TeacherSpecification(new SearchCriteria("dateOfBirth", date)))
                    .or(new TeacherSpecification(new SearchCriteria("hiredDate", date)));
        } catch (DateTimeParseException e){
            log.error(e.getMessage());
            throw new ApiException("Wrong search type. Please select DATE type");
        }
    }

    private Specification<Teacher> getSalarySpec(String searchText) {
        try{
            BigDecimal salary = BigDecimal.valueOf(Long.parseLong(searchText));
            return Specification.where(new TeacherSpecification(new SearchCriteria("salary", salary)));
        }catch (NumberFormatException e){
            log.error(e.getMessage());
            throw new ApiException("Wrong search type. Please select NUMBER type");
        }
    }

    private Specification<Teacher> getStringSpec(String searchText) {
        TeacherSpecification firstNameSpec = new TeacherSpecification(new SearchCriteria("firstName", searchText));
        TeacherSpecification lastNameSpec = new TeacherSpecification(new SearchCriteria("lastName", searchText));
        TeacherSpecification emailSpecification = new TeacherSpecification(new SearchCriteria("email", searchText));
        TeacherSpecification phoneNumberSpecification = new TeacherSpecification(new SearchCriteria("phoneNumber", searchText));
        TeacherSpecification aboutMeSpec = new TeacherSpecification(new SearchCriteria("aboutMe", searchText));
        TeacherSpecification highestDegreeSpec = new TeacherSpecification(new SearchCriteria("highestDegree", searchText));
        return Specification.where(firstNameSpec).or(lastNameSpec).or(emailSpecification)
                .or(phoneNumberSpecification).or(highestDegreeSpec).or(aboutMeSpec);
    }
}
