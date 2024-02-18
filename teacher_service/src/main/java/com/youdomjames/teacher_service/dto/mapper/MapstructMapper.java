package com.youdomjames.teacher_service.dto.mapper;

import com.youdomjames.teacher_service.domain.Address;
import com.youdomjames.teacher_service.domain.Teacher;
import com.youdomjames.teacher_service.dto.AddressDTO;
import com.youdomjames.teacher_service.dto.TeacherDTO;
import com.youdomjames.teacher_service.enumeration.Default;
import com.youdomjames.teacher_service.enumeration.Status;
import com.youdomjames.teacher_service.forms.AddressForm;
import com.youdomjames.teacher_service.forms.TeacherForm;
import org.mapstruct.*;
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
public abstract class MapstructMapper {
    public Teacher toTeacher(TeacherForm teacherForm) {
        Teacher teacher = Teacher.builder().build();
        BeanUtils.copyProperties(teacherForm, teacher);
        if (teacherForm.getStatus() == null) {
            teacher.setStatus(Status.INACTIVE);
        }
        if (teacherForm.getProfilePictureLink() == null) {
            teacher.setProfilePictureLink(Default.PICTURE_URL.getValue());
        }
        teacher.setAddress(toAddress(teacherForm.getAddress()));
        return teacher;
    }

    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "courseIds", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Teacher updateTeacher(TeacherForm teacherForm, @MappingTarget Teacher teacher);

    public abstract TeacherDTO toTeacherDTO(Teacher teacher);

    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract Address toAddress(AddressForm addressForm);
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void toAddress(@MappingTarget Address address, AddressForm addressForm);

    public abstract AddressDTO toAddressDTO(Address address);
}
