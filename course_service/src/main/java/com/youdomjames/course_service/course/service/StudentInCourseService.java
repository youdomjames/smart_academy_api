package com.youdomjames.course_service.course.service;

import com.youdomjames.course_service.course.domain.Course;
import com.youdomjames.course_service.course.domain.Student;
import com.youdomjames.course_service.enumeration.Status;
import com.youdomjames.course_service.exception.ApiException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public record StudentInCourseService(CourseService courseService) {
    public Student addStudent(String courseId, String studentId) {
        Course course = courseService.getCourseById(courseId);
        if (course.getStudents().containsKey(studentId)) {
            throw new ApiException("Student already present in class.");
        }
        course.getStudents().put(studentId, Student.builder()
                .statusInCourse(Status.ACTIVE)
                .coursePaymentStatus(Status.UNPAID)
                .build()
        );
        return getStudent(courseService.save(course), studentId);
    }

    public Student getStudentInCourse(String courseId, String studentId) {
        Course course = courseService.getCourseById(courseId);
        return getStudent(course, studentId);
    }

    public Student updateStudentStatusInCourse(String courseId, String studentId, Status statusInCourse, Status coursePaymentStatus) {
        Course course = courseService.getCourseById(courseId);
        studentPresenceInCourseCheck(course, studentId);
        if (statusInCourse != null) {
            course.getStudents().get(studentId)
                    .setStatusInCourse(statusInCourse);
        }
        if (coursePaymentStatus != null) {
            course.getStudents().get(studentId)
                    .setStatusInCourse(statusInCourse);
        }
        return getStudent(courseService.save(course), studentId);
    }

    public void deleteStudentFromCourse(String courseId, String studentId) {
        Course course = courseService.getCourseById(courseId);
        if (course.getStudents().remove(studentId) == null) {
            throw new ApiException("An error occurred. Student not removed from course.");
        }
        courseService.save(course);
    }

    public Map<String, Student> getStudentsInCourse(String courseId) {
        return courseService.getCourseById(courseId).getStudents();
    }

    private Student getStudent(Course course, String studentId) {
        studentPresenceInCourseCheck(course, studentId);
        return course.getStudents().get(studentId);
    }

    private void studentPresenceInCourseCheck(Course course, String studentId) {
        if (!course.getStudents().containsKey(studentId)) {
            throw new ApiException("Student not found in course");
        }
    }
}
