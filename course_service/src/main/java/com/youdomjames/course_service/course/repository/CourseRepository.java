package com.youdomjames.course_service.course.repository;

import com.youdomjames.course_service.course.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
    @Query("{ $or: [ { 'code': { $regex: ?0, $options: 'i' } }, { 'session': { $regex: ?0, $options: 'i' } }," +
            "{ 'year': { $regex: ?0, $options: 'i' } }, { 'teacherId': { $regex: ?0, $options: 'i' } }, { 'status': { $regex: ?0, $options: 'i' } } ] }")
    Page<Course> findAll(String filter, Pageable pageable);
}

