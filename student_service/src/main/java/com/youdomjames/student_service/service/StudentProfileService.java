package com.youdomjames.student_service.service;

import com.youdomjames.student_service.domain.Profile;
import com.youdomjames.student_service.domain.Student;
import com.youdomjames.student_service.dto.ProfileDTO;
import com.youdomjames.student_service.dto.mapper.MapstructMapper;
import com.youdomjames.student_service.exception.ApiException;
import com.youdomjames.student_service.form.ProfileForm;
import com.youdomjames.student_service.repository.StudentProfileRepository;
import com.youdomjames.student_service.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-22
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Service
public record StudentProfileService(StudentProfileRepository profileRepository,
                                    StudentRepository studentRepository,
                                    SearchService searchService,
                                    MapstructMapper mapper) {
    public ProfileDTO createStudent(ProfileForm form) {
        if (profileRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new ApiException("Student already present.");
        }
        Student student = Student.builder()
                .profile(mapper.toProfile(form))
                .build();
        return mapper.toProfileDTO(studentRepository.save(student).getProfile());
    }

    public ProfileDTO getStudentProfileById(String profileId) {
        return mapper.toProfileDTO(findById(profileId));
    }

    public Profile findById(String id) {
        return profileRepository.findById(id).orElseThrow(() -> new ApiException("Student profile Not Found"));
    }

    public Student getStudentByProfileId(String profileId) {
        return studentRepository.findByProfileId(profileId).orElseThrow(() -> new ApiException("Student not Found"));
    }

    public Set<ProfileDTO> getStudentProfilesByIds(Set<String> ids) {
        return mapper.toProfileDTOs(profileRepository.findAllById(ids));
    }

    public Page<ProfileDTO> searchStudentProfiles(String searchText, String searchType, String operation, int pageNumber, int pageSize) {
        int pageIndex = pageNumber - 1;
        return profileRepository.findAll(searchService.getSpecification(searchText, searchType, operation), PageRequest.of(pageIndex, pageSize))
                .map(mapper::toProfileDTO);
    }

    public ProfileDTO updateStudentProfileById(String id, ProfileForm form) {
        Profile profile = findById(id);
        Profile updatedStudentProfile = profileRepository.save(mapper.updateProfile(form, profile));
        return mapper.toProfileDTO(updatedStudentProfile);
    }

    public void deleteStudentByProfileId(String id) {
        if (!profileRepository.existsById(id)) {
            throw new ApiException("Student not present. Nothing was deleted");
        }
        profileRepository.deleteById(id);
        if (profileRepository.existsById(id)) {
            throw new ApiException("An Error occurred. Student not deleted");
        }
    }

    public void updateStudent(Student student){
        studentRepository.save(student);
    }
}
