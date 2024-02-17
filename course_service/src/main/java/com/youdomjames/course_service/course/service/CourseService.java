package com.youdomjames.course_service.course.service;

import com.youdomjames.course_service.course.domain.Course;
import com.youdomjames.course_service.course.dto.CourseDTO;
import com.youdomjames.course_service.course.dto.mapper.CourseDTOMapper;
import com.youdomjames.course_service.course.repository.CourseRepository;
import com.youdomjames.course_service.exception.ApiException;
import com.youdomjames.course_service.forms.CourseForm;
import com.youdomjames.course_service.mapstruct.MapstructMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static com.youdomjames.course_service.course.dto.mapper.CourseDTOMapper.toCourse;
import static com.youdomjames.course_service.course.dto.mapper.CourseDTOMapper.toCourseDTO;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
public record CourseService(CourseRepository courseRepository, MapstructMapper mapstructMapper) {
    public CourseDTO addCourse(CourseForm courseForm) {
        Course course = toCourse(courseForm);
        course.setCode(generateCourseCode(course));
        course.setCreatedAt(now());
        course.setModifiedAt(now());
        return toCourseDTO(save(course));
    }

    public Course getCourseById(String courseId) throws ApiException {
        return courseRepository.findById(courseId).orElseThrow(() -> new ApiException("No course found"));
    }

    public Page<CourseDTO> getAllCoursesBySearchParam(String searchText, int pageNumber, int pageSize) {
        if (searchText.isEmpty()) searchText = "";
        int pageIndex = pageNumber - 1;
        courseRepository.findAll(searchText, PageRequest.of(pageIndex, pageSize)).toList().forEach(System.out::println);
        return courseRepository.findAll(searchText, PageRequest.of(pageIndex, pageSize)).map(CourseDTOMapper::toCourseDTO);
    }

    public CourseDTO updateCourse(String id, CourseForm courseForm) throws ApiException {
        Course course = getCourseById(id);
        mapstructMapper.updateCourse(course, courseForm);
        if (courseForm.getName() != null || courseForm.getSession() != null || courseForm.getYear() != null) {
            course.setCode(generateCourseCode(course));
        }
        course.setModifiedAt(now());
        return toCourseDTO(save(course));
    }

    public void deleteCourse(String id) throws ApiException {
        courseRepository.deleteById(id);
        log.info("Course {} deleted", id);
        if (courseRepository.findById(id).isPresent()) {
            throw new ApiException("An error occurred. Course not deleted.");
        }
    }

    public boolean isCoursePresent(String courseId) {
        return courseRepository.existsById(courseId);
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    private String generateCourseCode(Course course) {
        return course.getName().toUpperCase().substring(0, 4)
                .concat("_")
                .concat(course.getSession().name().toUpperCase().substring(0, 4))
                .concat("_")
                .concat(course.getYear());
    }
}
