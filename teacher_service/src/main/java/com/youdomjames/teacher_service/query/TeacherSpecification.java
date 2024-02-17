package com.youdomjames.teacher_service.query;

import com.youdomjames.teacher_service.domain.Teacher;
import com.youdomjames.teacher_service.exception.ApiException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-17
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Slf4j
public class TeacherSpecification implements Specification<Teacher> {
    private final SearchCriteria searchCriteria;

    public TeacherSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return switch (searchCriteria.getType()){
            case "STRING" -> getStringCriteriaBuilder(root, criteriaBuilder);
            case "NUMBER" -> getNumericCriteriaBuilder(root, criteriaBuilder);
            case "DATE" -> getDateCriteriaBuilder(root, criteriaBuilder);
            case "ENUM" -> getEnumCriteriaBuilder(root, criteriaBuilder);
            case "ADDRESS" -> getAddressCriteriaBuilder(root, criteriaBuilder);
            default -> throw new ApiException("Wrong search type. Please try again");
        };

    }

    private Predicate getAddressCriteriaBuilder(Root<Teacher> root, CriteriaBuilder criteriaBuilder) {
        return null;
    }

    private Predicate getEnumCriteriaBuilder(Root<Teacher> root, CriteriaBuilder criteriaBuilder) {
        return null;
    }

    private Predicate getDateCriteriaBuilder(Root<Teacher> root, CriteriaBuilder criteriaBuilder) {
        return null;
    }

    private Predicate getNumericCriteriaBuilder(Root<Teacher> root, CriteriaBuilder criteriaBuilder) {
        try {
            return switch (searchCriteria.getOperation()){
                case "<" -> criteriaBuilder.lt(
                        root.get(searchCriteria.getKey()), (Number) searchCriteria.getValue()
                );
                case ">" -> criteriaBuilder.gt(
                        root.get(searchCriteria.getKey()), (Number) searchCriteria.getValue()
                );
                case ":" -> criteriaBuilder.equal(
                        root.get(searchCriteria.getKey()), searchCriteria.getValue()
                );
                default -> throw new ApiException("Operation not supported for this filter");
            };
        } catch (IllegalArgumentException | IllegalStateException e){
            log.error(e.getLocalizedMessage());
            throw new ApiException("An error occurred");
        }
    }

    private Predicate getStringCriteriaBuilder(Root<Teacher> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(
                root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%");
    }


}
