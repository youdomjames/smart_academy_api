package com.youdomjames.course_service.assignment.service;

import com.youdomjames.course_service.assignment.domain.Assignment;
import com.youdomjames.course_service.assignment.dto.AssignmentDTO;
import com.youdomjames.course_service.assignment.dto.StudentAssignmentDTO;
import com.youdomjames.course_service.assignment.dto.mapper.AssignmentMapper;
import com.youdomjames.course_service.assignment.repository.AssignmentRepository;
import com.youdomjames.course_service.course.service.CourseService;
import com.youdomjames.course_service.enumeration.Status;
import com.youdomjames.course_service.exception.ApiException;
import com.youdomjames.course_service.forms.AssignmentForm;
import com.youdomjames.course_service.mapstruct.MapstructMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

import static com.youdomjames.course_service.assignment.dto.mapper.AssignmentMapper.*;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
public record AssignmentService(AssignmentRepository assignmentRepository, CourseService courseService,
                                MapstructMapper mapstructMapper, RedisTemplate<String, Object> redisTemplate) {
    public AssignmentDTO createAssignment(AssignmentForm assignmentForm) {
        if (!courseService.isCoursePresent(assignmentForm.getCourseId())) {
            throw new ApiException("The course provided is not present. Please Add new course or select another one before creating an assignment");
        }
        Assignment assignment = toAssignment(assignmentForm);
        if (assignment.getStartDate().equals(now()) || assignment.getStartDate().isBefore(now())) {
            assignment.setStatus(Status.IN_PROGRESS);
        } else
            assignment.setStatus(Status.NOT_STARTED);
        assignment.setCode(generateCode(assignment));
        assignment.setCreatedAt(now());

        return toAssignmentDTO(save(assignment));
    }

    public AssignmentDTO getAssignmentDTOById(String id) {
        return toAssignmentDTO(getAssignmentById(id));
    }

    public Assignment save(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public Assignment getAssignmentById(String id) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(id))) {
            log.info("Assignment {} found in cache", id);
            return (Assignment) redisTemplate.opsForValue().get(id);
        }
        Assignment assignment = assignmentRepository.findById(id).orElseThrow(() -> new ApiException("Assignment not found"));
        redisTemplate.opsForValue().set(id, assignment, Duration.of(15, ChronoUnit.MINUTES));
        log.info("Assignment {} added to cache", id);
        return assignment;
    }

    public Page<AssignmentDTO> getAssignments(String searchText, int pageNumber, int pageSize) {
        int pageIndex = pageNumber - 1;
        return assignmentRepository.findAll(searchText, PageRequest.of(pageIndex, pageSize))
                .map(AssignmentMapper::toAssignmentDTO);
    }

    public AssignmentDTO updateAssignmentById(String id, AssignmentForm assignmentForm) {
        Assignment assignment = getAssignmentById(id);
        mapstructMapper.updateAssignment(assignment, assignmentForm);
        if (assignmentForm.getCourseId() != null) {
            throw new ApiException("You cannot change the course related to this assignment. \n" +
                    "Please set the status to canceled instead, and create a new assignment");
        }
        assignment.setModifiedAt(now());
        Assignment updatedAssignment = save(assignment);
        redisTemplate.opsForValue().set(id, updatedAssignment, Duration.of(15, ChronoUnit.MINUTES));
        return toAssignmentDTO(updatedAssignment);
    }

    public void deleteAssignmentById(String id) {
        assignmentRepository.deleteById(id);
        log.info("Assignment {} deleted", id);
        if (assignmentRepository.findById(id).isPresent()) {
            throw new ApiException("An error occurred. Assignment not deleted.");
        }
        redisTemplate.delete(id);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(id))) {
            throw new ApiException("An error occurred. Assignment not deleted from cache.");
        }
    }

    private String generateCode(Assignment assignment) {
        String assignmentRank = String.valueOf(assignmentRepository.countByCourseId(assignment.getCourseId()) + 1);
        return String.join("_", "ASSIGN",
                courseService.getCourseById(assignment.getCourseId()).getCode(),
                assignmentRank);
    }

    public void changeAssignmentsStatusPeriodically() {
        assignmentRepository.findAll()
                .forEach(this::changeAssignmentStatusIfApplicable);
    }

    private void changeAssignmentStatusIfApplicable(Assignment assignment) {
        Assignment updatedAssignment = new Assignment();
        if (assignment.getStatus().equals(Status.IN_PROGRESS) && (assignment.getDeadline().isBefore(now()) || assignment.getDeadline().equals(now()))) {
            assignment.setStatus(Status.ENDED);
            assignment.setModifiedAt(now());
            updatedAssignment = assignmentRepository.save(assignment);
        }
        if (assignment.getStatus().equals(Status.NOT_STARTED) && (assignment.getStartDate().isBefore(now()) || assignment.getStartDate().equals(now()))) {
            assignment.setStatus(Status.IN_PROGRESS);
            assignment.setModifiedAt(now());
            updatedAssignment = assignmentRepository.save(assignment);
        }
        if (Boolean.TRUE.equals(redisTemplate.hasKey(assignment.getId()))) {
            redisTemplate.opsForValue().set(assignment.getId(), updatedAssignment, Duration.of(15, ChronoUnit.MINUTES));
        }
    }

    public void updateAssignmentCompletionRates() {
        assignmentRepository.findAll().forEach(this::updateCompletionRate);
    }

    private void updateCompletionRate(Assignment assignment) {
        long totalOfActiveStudentsInCourse =
                courseService.getCourseById(assignment.getCourseId()).getStudents().values().stream()
                        .filter(student -> student.getStatusInCourse().equals(Status.ACTIVE)).count();
        long totalOfStudentAssignmentUploaded =
                assignment.getStudentAssignments().keySet().size();

        BigDecimal completionRate = BigDecimal.valueOf(totalOfStudentAssignmentUploaded)
                .divide(BigDecimal.valueOf(totalOfActiveStudentsInCourse), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        assignment.setStudentCompletionRate(completionRate);
        assignment.setModifiedAt(now());
        Assignment updatedAssignment = assignmentRepository.save(assignment);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(assignment.getId()))) {
            redisTemplate.opsForValue().set(assignment.getId(), updatedAssignment, Duration.of(15, ChronoUnit.MINUTES));
        }
    }

    public Set<StudentAssignmentDTO> getAssignmentsByListOfCourseIds(String studentId, Set<String> courseIds) {
        validateCourseIds(courseIds);
        return courseIds.stream().parallel().map(id -> assignmentRepository.findByCourseId(id).orElseThrow(() -> new ApiException("No assignment found for the course with id: " + id)))
                .map(assignment -> toStudentAssignmentDTO(assignment, assignment.getStudentAssignments().get(studentId)))
                .collect(Collectors.toSet());
    }

    private void validateCourseIds(Set<String> courseIds) {
        if (courseIds.isEmpty()) {
            throw new ApiException("The list of course ids cannot be empty");
        }
        courseIds.forEach(courseId -> {
            if (assignmentRepository.findByCourseId(courseId).isEmpty()) {
                throw new ApiException("No assignment found for the course with id: " + courseId);
            }
            if (!courseService.isCoursePresent(courseId)) {
                throw new ApiException("The course provided is not present. Please Add new course or select another one before creating an assignment");
            }
        });
    }
}
