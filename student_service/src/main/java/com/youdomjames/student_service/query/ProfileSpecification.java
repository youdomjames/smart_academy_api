package com.youdomjames.student_service.query;

import com.youdomjames.student_service.domain.Profile;
import com.youdomjames.student_service.exception.ApiException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-17
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Slf4j
public class ProfileSpecification implements Specification<Profile> {
    private final SearchCriteria searchCriteria;

    public ProfileSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<Profile> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        return switch (searchCriteria.getType()) {
            case "STRING" -> getStringCriteriaBuilder(root, criteriaBuilder);
            case "DATE" -> getDateCriteriaBuilder(root, criteriaBuilder);
            case "ENUM" -> getEnumCriteriaBuilder(root, criteriaBuilder);
            case "ADDRESS" -> getAddressCriteriaBuilder(root, criteriaBuilder);
            default -> throw new ApiException("Wrong search type. Please try again");
        };

    }

    private Predicate getAddressCriteriaBuilder(Root<Profile> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(
                root.get("address").get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%");
    }

    private Predicate getEnumCriteriaBuilder(Root<Profile> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(
                root.get(searchCriteria.getKey()), searchCriteria.getValue());
    }

    private Predicate getDateCriteriaBuilder(Root<Profile> root, CriteriaBuilder criteriaBuilder) {
        try {
            return switch (searchCriteria.getOperation()) {
                case "<" -> criteriaBuilder.lessThan(
                        root.get(searchCriteria.getKey()), (LocalDate) searchCriteria.getValue()
                );
                case ">" -> criteriaBuilder.greaterThan(
                        root.get(searchCriteria.getKey()), (LocalDate) searchCriteria.getValue()
                );
                case ":" -> criteriaBuilder.equal(
                        root.get(searchCriteria.getKey()), searchCriteria.getValue()
                );
                default -> throw new ApiException("Operation not supported for this filter");
            };
        } catch (IllegalArgumentException | IllegalStateException | NullPointerException e) {
            log.error(e.getLocalizedMessage());
            throw new ApiException("An error occurred");
        }
    }

    private Predicate getStringCriteriaBuilder(Root<Profile> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(
                root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%");
    }


}
