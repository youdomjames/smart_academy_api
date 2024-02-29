package com.youdomjames.student_service.service.kafka;

import com.youdomjames.student_service.service.course.CourseService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-28
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Component
public record KafkaService(CourseService courseService) {

    @KafkaListener(topics = "student_course", groupId = "student_service")
    public void consumeCourseMessage(String record) {
//       if (Objects.nonNull(record) && record.key().toString().equals("ADD")) {
        courseService.addCourse(record);
//       } else if (Objects.nonNull(record) && record.key().toString().equals("REMOVE")) {
//           courseService.removeCourse(record.value().toString());
//       }
    }
}
