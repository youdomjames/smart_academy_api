package com.youdomjames.teacher_service.dto.mapper;

import com.youdomjames.teacher_service.domain.Address;
import com.youdomjames.teacher_service.domain.Teacher;
import com.youdomjames.teacher_service.dto.AddressDTO;
import com.youdomjames.teacher_service.dto.TeacherDTO;
import com.youdomjames.teacher_service.enumeration.Default;
import com.youdomjames.teacher_service.enumeration.Status;
import com.youdomjames.teacher_service.forms.AddressForm;
import com.youdomjames.teacher_service.forms.TeacherForm;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.BeanUtils;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-17
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Mapper(componentModel = "spring")
public interface MapStructMapper {
    default Teacher toTeacher(TeacherForm teacherForm) {
        Teacher teacher = Teacher.builder().build();
        BeanUtils.copyProperties(teacherForm, teacher);
        if (teacherForm.getStatus() == null) {
            teacher.setStatus(Status.INACTIVE);
        }
        if (teacherForm.getProfilePictureLink() == null) {
            teacher.setProfilePictureLink(Default.PICTURE_URL.getValue());
        }
        teacher.setAddress(addressFormtoAddress(teacherForm.getAddress()));
        return teacher;
    }

    Teacher teacherDTOtoTeacher(TeacherDTO teacherDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Teacher updateTeacher(@MappingTarget Teacher teacher, TeacherForm teacherForm);

    TeacherDTO teachertoTeacherDTO(Teacher teacher);

    Address addressFormtoAddress(AddressForm addressForm);

    Address addressDTOtoAddress(AddressDTO addressDTO);

    AddressDTO addresstoAddressDTO(Address address);
}
