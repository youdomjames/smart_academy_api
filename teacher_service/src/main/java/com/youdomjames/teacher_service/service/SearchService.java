package com.youdomjames.teacher_service.service;

import com.youdomjames.teacher_service.domain.Teacher;
import com.youdomjames.teacher_service.enumeration.Gender;
import com.youdomjames.teacher_service.enumeration.Status;
import com.youdomjames.teacher_service.exception.ApiException;
import com.youdomjames.teacher_service.query.SearchCriteria;
import com.youdomjames.teacher_service.query.TeacherSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

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
@Component
public class SearchService {
    public Specification<Teacher> getSpecification(String searchType, String searchText, String operation){
         return switch (searchType) {
            case "STRING" -> getStringSpec(searchText, searchType);
            case "NUMBER" -> getNumericSpec(searchType, searchText, operation);
            case "DATE" -> getDateSpec(searchText, searchType, operation);
            case "ENUM" -> getEnumSpec(searchText, searchType);
            case "ADDRESS" -> addressSpec(searchText, searchType);
            default -> throw new ApiException("Wrong search type. Please try again");
        };
    }

    private Specification<Teacher> addressSpec(String searchText, String searchType) {
        TeacherSpecification streetSpec = new TeacherSpecification(new SearchCriteria(searchType,"street", searchText));
        TeacherSpecification citySpec = new TeacherSpecification(new SearchCriteria(searchType,"city", searchText));
        TeacherSpecification stateSpec = new TeacherSpecification(new SearchCriteria(searchType,"state", searchText));
        TeacherSpecification postalCodeSpec = new TeacherSpecification(new SearchCriteria(searchType,"postalCode", searchText));
        TeacherSpecification apartmentNumberSpec = new TeacherSpecification(new SearchCriteria(searchType,"apartmentNumber", searchText));
        return Specification.where(streetSpec).or(citySpec).or(stateSpec)
                .or(postalCodeSpec).or(apartmentNumberSpec);
    }

    private Specification<Teacher> getEnumSpec(String searchText, String searchType) {
        try{
            Status status = Status.valueOf(searchText);
            return Specification.where(new TeacherSpecification( new SearchCriteria(searchType,"status", status)));
        } catch (IllegalArgumentException e){
            Gender gender = Gender.valueOf(searchText);
            return Specification.where(new TeacherSpecification(new SearchCriteria(searchType,"gender", gender)));
        } catch (Exception e){
            log.error(e.getMessage());
            throw new ApiException("Wrong search type. Please select ENUM type");
        }
    }

    private Specification<Teacher> getDateSpec(String searchText, String searchType, String operation) {
        try{
            LocalDate date = LocalDate.parse(searchText);
            return Specification.where(new TeacherSpecification(new SearchCriteria(searchType, operation, "dateOfBirth", date)))
                    .or(new TeacherSpecification(new SearchCriteria(searchType, operation,"hiredDate", date)));
        } catch (DateTimeParseException e){
            log.error(e.getMessage());
            throw new ApiException("Wrong search type. Please select DATE type");
        }
    }

    private Specification<Teacher> getNumericSpec(String searchType, String searchText, String operation) {
        try{
            BigDecimal salary = BigDecimal.valueOf(Long.parseLong(searchText));
            return Specification.where(new TeacherSpecification(new SearchCriteria(searchType, operation, "salary", salary)));
        }catch (NumberFormatException e){
            log.error(e.getMessage());
            throw new ApiException("Wrong search type. Please select NUMBER type");
        }
    }

    private Specification<Teacher> getStringSpec(String searchText, String searchType) {
        TeacherSpecification firstNameSpec = new TeacherSpecification(new SearchCriteria(searchType,"firstName", searchText));
        TeacherSpecification lastNameSpec = new TeacherSpecification(new SearchCriteria(searchType,"lastName", searchText));
        TeacherSpecification emailSpecification = new TeacherSpecification(new SearchCriteria(searchType,"email", searchText));
        TeacherSpecification phoneNumberSpecification = new TeacherSpecification(new SearchCriteria(searchType,"phoneNumber", searchText));
        TeacherSpecification aboutMeSpec = new TeacherSpecification(new SearchCriteria(searchType,"aboutMe", searchText));
        TeacherSpecification highestDegreeSpec = new TeacherSpecification(new SearchCriteria(searchType,"highestDegree", searchText));
        return Specification.where(firstNameSpec).or(lastNameSpec).or(emailSpecification)
                .or(phoneNumberSpecification).or(highestDegreeSpec).or(aboutMeSpec);
    }
}
