package com.youdomjames.course_service.mapstruct;

import com.youdomjames.course_service.assignment.domain.Assignment;
import com.youdomjames.course_service.course.domain.Course;
import com.youdomjames.course_service.forms.AssignmentForm;
import com.youdomjames.course_service.forms.CourseForm;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MapstructMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCourse(@MappingTarget Course course, CourseForm courseForm);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAssignment(@MappingTarget Assignment assignment, AssignmentForm assignmentForm);
}
