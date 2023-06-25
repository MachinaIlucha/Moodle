package com.illiapinchuk.moodle.persistence.repository;

import com.illiapinchuk.moodle.persistence.entity.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

/** Basic repository for course. */
public interface CourseRepository extends MongoRepository<Course, String> {}