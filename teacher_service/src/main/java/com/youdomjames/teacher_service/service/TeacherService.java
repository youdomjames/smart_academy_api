package com.youdomjames.teacher_service.service;

import com.youdomjames.teacher_service.domain.Payment;
import com.youdomjames.teacher_service.domain.Teacher;
import com.youdomjames.teacher_service.dto.TeacherDTO;
import com.youdomjames.teacher_service.dto.mapper.MapstructMapper;
import com.youdomjames.teacher_service.enumeration.Topic;
import com.youdomjames.teacher_service.exception.ApiException;
import com.youdomjames.teacher_service.exception.KafkaRecordHandlingException;
import com.youdomjames.teacher_service.forms.AssignmentResultsForm;
import com.youdomjames.teacher_service.forms.TeacherForm;
import com.youdomjames.teacher_service.repository.TeacherRepository;
import com.youdomjames.teacher_service.service.kafka.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

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
public record TeacherService(TeacherRepository repository, MapstructMapper mapper,
                             SearchService searchService, @Lazy KafkaService kafkaService) {
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

    public Set<String> getTeacherCourses(String id) {
        return findById(id).getCourseIds();
    }

    public void sendResults(String teacherId, AssignmentResultsForm resultsForm) {
        Teacher teacher = findById(teacherId);
        if (!teacher.getCourseIds().contains(resultsForm.getCourseId())) {
            throw new ApiException("Teacher is not assigned to this course.");
        }
        if (resultsForm.getStudentScores().containsKey(null) || resultsForm.getStudentScores().containsValue(null)) {
            throw new ApiException("All students should be linked to each results provided");
        }
        String key = String.join("_", resultsForm.getCourseId(), resultsForm.getAssignmentId());
        //TODO: Transaction management
        resultsForm.getStudentScores().entrySet().stream()
                .map(entry -> new ProducerRecord<>(Topic.RESULTS.name(), key, getRecordValue(entry)))
                .forEach(kafkaService::send);
    }

    private String getRecordValue(Map.Entry<String, BigDecimal> entry) {
        return String.join("_", entry.getKey(), entry.getValue().toPlainString());
    }

    public void handleSalaryPayment(String record) {
        try {
            String[] splitRecord = record.split("_");
            if (splitRecord.length != 3) {
                throw new KafkaRecordHandlingException("Record handling not supported. Expected split length 3, but got " + splitRecord.length);
            }
            String teacherId = splitRecord[0];
            BigDecimal amount = BigDecimal.valueOf(Long.parseLong(splitRecord[1]));
            LocalDateTime paymentDate = LocalDateTime.parse(splitRecord[2]);

            Teacher teacher = findById(teacherId);
            teacher.getPayments().add(
                    Payment.builder()
                            .amount(amount)
                            .paymentDate(paymentDate)
                            .build()
            );
            repository.save(teacher);
            log.info("Salary payment successfully handled. teacherId= {} amount= {}", teacher.getId(), amount);
        } catch (NumberFormatException | PatternSyntaxException | DateTimeParseException e) {
            log.error(e.getLocalizedMessage());
            throw new KafkaRecordHandlingException("Unable to handle data");
        }
    }
}
