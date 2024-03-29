package com.youdomjames.course_service.assignment.dto.mapper;

import com.youdomjames.course_service.assignment.domain.Assignment;
import com.youdomjames.course_service.assignment.domain.StudentAssignment;
import com.youdomjames.course_service.assignment.dto.AssignmentDTO;
import com.youdomjames.course_service.assignment.dto.StudentAssignmentDTO;
import com.youdomjames.course_service.forms.AssignmentForm;
import org.springframework.beans.BeanUtils;

public class AssignmentMapper {

    public static Assignment toAssignment(AssignmentForm assignmentForm) {
        Assignment assignment = new Assignment();
        BeanUtils.copyProperties(assignmentForm, assignment);
        return assignment;
    }

    public static AssignmentDTO toAssignmentDTO(Assignment assignment) {
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        BeanUtils.copyProperties(assignment, assignmentDTO);
        return assignmentDTO;
    }

    public static StudentAssignmentDTO toStudentAssignmentDTO(Assignment assignment, StudentAssignment studentAssignment) {
        StudentAssignmentDTO studentAssignmentDTO = new StudentAssignmentDTO();
        BeanUtils.copyProperties(studentAssignment, studentAssignmentDTO);
        studentAssignmentDTO.setAssignment(toAssignmentDTO(assignment));
        return studentAssignmentDTO;

    }
}
