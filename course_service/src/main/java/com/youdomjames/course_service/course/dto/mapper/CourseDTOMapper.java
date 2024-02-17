package com.youdomjames.course_service.course.dto.mapper;

import com.youdomjames.course_service.course.domain.Course;
import com.youdomjames.course_service.course.dto.CourseDTO;
import com.youdomjames.course_service.enumeration.Default;
import com.youdomjames.course_service.forms.CourseForm;
import org.springframework.beans.BeanUtils;

public class CourseDTOMapper {

    public static Course toCourse(CourseDTO courseDTO) {
        Course course = new Course();
        BeanUtils.copyProperties(courseDTO, course);
        if (course.getLogoUrl().isEmpty() || course.getLogoUrl().isBlank()) {
            course.setLogoUrl(Default.COURSE_URL.name());
        }
        return course;
    }

    public static Course toCourse(CourseForm courseForm) {
        Course course = new Course();
        BeanUtils.copyProperties(courseForm, course);
        if (course.getLogoUrl() == null || course.getLogoUrl().isEmpty() || course.getLogoUrl().isBlank()) {
            course.setLogoUrl(Default.COURSE_URL.getValue());
        }
        return course;
    }

    public static CourseDTO toCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        BeanUtils.copyProperties(course, courseDTO);
        return courseDTO;
    }

}
