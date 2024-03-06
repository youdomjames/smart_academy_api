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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.youdomjames.course_service.course.dto.mapper.CourseDTOMapper.toCourse;
import static com.youdomjames.course_service.course.dto.mapper.CourseDTOMapper.toCourseDTO;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
public record CourseService(CourseRepository courseRepository, MapstructMapper mapstructMapper,
                            RedisTemplate<String, Object> redisTemplate) {
    public CourseDTO addCourse(CourseForm courseForm) {
        Course course = toCourse(courseForm);
        course.setCode(generateCourseCode(course));
        course.setCreatedAt(now());
        course.setModifiedAt(now());
        Course savedCourse = save(course);
        return toCourseDTO(savedCourse);
    }

    public Course getCourseById(String courseId) throws ApiException {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(courseId))) {
            log.info("Course {} found in cache", courseId);
            return (Course) redisTemplate.opsForValue().get(courseId);
        }
        Course foundCourse = courseRepository.findById(courseId).orElseThrow(() -> new ApiException("Course not found"));
        redisTemplate.opsForValue().set(courseId, foundCourse, Duration.of(15, ChronoUnit.MINUTES));
        log.info("Course {} added to cache", courseId);
        return foundCourse;
    }

    public CourseDTO getCourseDTOById(String courseId) {
        return toCourseDTO(getCourseById(courseId));
    }

    public Page<CourseDTO> getAllCoursesBySearchParam(String searchText, int pageNumber, int pageSize) {
        if (searchText.isEmpty()) searchText = "";
        int pageIndex = pageNumber - 1;
        return courseRepository.findAll(searchText, PageRequest.of(pageIndex, pageSize)).map(CourseDTOMapper::toCourseDTO);
    }

    public List<CourseDTO> getAllCoursesByIds(List<String> courseIds) {
        return courseRepository.findAllById(courseIds).stream().parallel().map(CourseDTOMapper::toCourseDTO).toList();
    }

    public CourseDTO updateCourse(String id, CourseForm courseForm) throws ApiException {
        Course course = getCourseById(id);
        mapstructMapper.updateCourse(course, courseForm);
        if (courseForm.getName() != null || courseForm.getSession() != null || courseForm.getYear() != null) {
            course.setCode(generateCourseCode(course));
        }
        course.setModifiedAt(now());
        CourseDTO updatedCourse = toCourseDTO(save(course));
        redisTemplate.opsForValue().set(id, updatedCourse, Duration.of(15, ChronoUnit.MINUTES));
        return updatedCourse;
    }

    public void deleteCourse(String id) throws ApiException {
        courseRepository.deleteById(id);
        log.info("Course {} deleted", id);
        if (courseRepository.findById(id).isPresent()) {
            throw new ApiException("An error occurred. Course not deleted.");
        }
        redisTemplate.delete(id);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(id))) {
            throw new ApiException("An error occurred. Course not deleted from cache.");
        }
    }

    public boolean isCoursePresent(String courseId) {
        return courseRepository.existsById(courseId);
    }

    public Course save(Course course) {
        Course savedCourse = courseRepository.save(course);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(savedCourse.getId()))) {
            redisTemplate.opsForValue().set(savedCourse.getId(), savedCourse, Duration.of(15, ChronoUnit.MINUTES));
            log.info("Course {} updated in cache", savedCourse.getId());
        }
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
