package com.youdomjames.course_service.mapstruct;

import com.youdomjames.course_service.assignment.domain.Assignment;
import com.youdomjames.course_service.course.domain.Course;
import com.youdomjames.course_service.forms.AssignmentForm;
import com.youdomjames.course_service.forms.CourseForm;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MapstructMapper {
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "code", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCourse(@MappingTarget Course course, CourseForm courseForm);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAssignment(@MappingTarget Assignment assignment, AssignmentForm assignmentForm);
}
