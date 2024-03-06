package com.youdomjames.student_service.dto.mapper;

import com.youdomjames.student_service.domain.Address;
import com.youdomjames.student_service.domain.Profile;
import com.youdomjames.student_service.domain.Student;
import com.youdomjames.student_service.dto.AddressDTO;
import com.youdomjames.student_service.dto.ProfileDTO;
import com.youdomjames.student_service.dto.StudentDTO;
import com.youdomjames.student_service.enumerations.Default;
import com.youdomjames.student_service.enumerations.Status;
import com.youdomjames.student_service.form.AddressForm;
import com.youdomjames.student_service.form.ProfileForm;
import org.mapstruct.*;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-22
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Mapper(componentModel = "spring")
public abstract class MapstructMapper {
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public Profile toProfile(ProfileForm profileForm) {
        Profile profile = Profile.builder().build();
        BeanUtils.copyProperties(profileForm, profile);
        if (Objects.isNull(profileForm.getProfilePictureLink())) {
            profile.setProfilePictureLink(Default.PICTURE_URL.getValue());
        }
        if (Objects.isNull(profileForm.getStatus())) {
            profile.setStatus(Status.ACTIVE);
        }
        profile.setAddress(toAddress(profileForm.getAddress()));
        return profile;
    }

    ;

    public abstract ProfileDTO toProfileDTO(Profile profile);

    public abstract AddressDTO toAddressDTO(Address address);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract Address toAddress(AddressForm addressForm);

    @Mapping(target = "profileDTO", source = "profile")
    public abstract StudentDTO toStudentDTO(Student student);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Profile updateProfile(ProfileForm form, @MappingTarget Profile profile);

    public Set<ProfileDTO> toProfileDTOs(List<Profile> profiles) {
        return profiles.stream().map(this::toProfileDTO).collect(Collectors.toSet());
    }
}
