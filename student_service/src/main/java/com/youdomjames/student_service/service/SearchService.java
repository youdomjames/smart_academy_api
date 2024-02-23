package com.youdomjames.student_service.service;

import com.youdomjames.student_service.domain.Profile;
import com.youdomjames.student_service.enumerations.Gender;
import com.youdomjames.student_service.enumerations.Status;
import com.youdomjames.student_service.exception.ApiException;
import com.youdomjames.student_service.query.ProfileSpecification;
import com.youdomjames.student_service.query.SearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-23
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Slf4j
@Component
public record SearchService() {
    public Specification<Profile> getSpecification(String searchText, String searchType, String operation) {
        return switch (searchType) {
            case "STRING" -> getStringSpec(searchText, searchType);
            case "DATE" -> getDateSpec(searchText, searchType, operation);
            case "ENUM" -> getEnumSpec(searchText, searchType);
            case "ADDRESS" -> addressSpec(searchText, searchType);
            default -> throw new ApiException("Wrong search type. Please try again");
        };
    }

    private Specification<Profile> addressSpec(String searchText, String searchType) {
        ProfileSpecification streetSpec = new ProfileSpecification(new SearchCriteria(searchType, "street", searchText));
        ProfileSpecification citySpec = new ProfileSpecification(new SearchCriteria(searchType, "city", searchText));
        ProfileSpecification stateSpec = new ProfileSpecification(new SearchCriteria(searchType, "state", searchText));
        ProfileSpecification postalCodeSpec = new ProfileSpecification(new SearchCriteria(searchType, "postalCode", searchText));
        ProfileSpecification apartmentNumberSpec = new ProfileSpecification(new SearchCriteria(searchType, "apartmentNumber", searchText));
        return Specification.where(streetSpec).or(citySpec).or(stateSpec)
                .or(postalCodeSpec).or(apartmentNumberSpec);
    }

    private Specification<Profile> getEnumSpec(String searchText, String searchType) {
        try {
            Status status = Status.valueOf(searchText);
            return Specification.where(new ProfileSpecification(new SearchCriteria(searchType, "status", status)));
        } catch (IllegalArgumentException e) {
            Gender gender = Gender.valueOf(searchText);
            return Specification.where(new ProfileSpecification(new SearchCriteria(searchType, "gender", gender)));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("Wrong search type. Please select ENUM type");
        }
    }

    private Specification<Profile> getDateSpec(String searchText, String searchType, String operation) {
        try {
            LocalDate date = LocalDate.parse(searchText);
            return Specification.where(new ProfileSpecification(new SearchCriteria(searchType, operation, "dateOfBirth", date)));
        } catch (DateTimeParseException e) {
            log.error(e.getMessage());
            throw new ApiException("Wrong search type. Please select DATE type");
        }
    }

    private Specification<Profile> getStringSpec(String searchText, String searchType) {
        ProfileSpecification firstNameSpec = new ProfileSpecification(new SearchCriteria(searchType, "firstName", searchText));
        ProfileSpecification lastNameSpec = new ProfileSpecification(new SearchCriteria(searchType, "lastName", searchText));
        ProfileSpecification emailSpecification = new ProfileSpecification(new SearchCriteria(searchType, "email", searchText));
        ProfileSpecification phoneNumberSpecification = new ProfileSpecification(new SearchCriteria(searchType, "phoneNumber", searchText));
        ProfileSpecification aboutMeSpec = new ProfileSpecification(new SearchCriteria(searchType, "aboutMe", searchText));
        return Specification.where(firstNameSpec).or(lastNameSpec).or(emailSpecification)
                .or(phoneNumberSpecification).or(aboutMeSpec);
    }
}
