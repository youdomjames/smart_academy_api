package com.youdomjames.course_service.assignment.repository;

import com.youdomjames.course_service.assignment.domain.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AssignmentRepository extends MongoRepository<Assignment, String> {
    long countByCourseId(String courseId);

    @Query("{ $or: [ { 'code': { $regex: ?0, $options: 'i' } }, " +
            "{ 'status': { $regex: ?0, $options: 'i' } }, { 'courseId': { $regex: ?0, $options: 'i' } }, " +
            "{ 'studentCompletionRate': { $regex: ?0, $options: 'i' } }, { 'startDate': { $regex: ?0, $options: 'i' } }, " +
            "{ 'deadline': { $regex: ?0, $options: 'i' } }, { 'averageScore': { $regex: ?0, $options: 'i' } } ] }")
    Page<Assignment> findAll(String filter, Pageable pageable);
}
