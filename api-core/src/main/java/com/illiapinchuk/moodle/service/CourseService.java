package com.illiapinchuk.moodle.service;

import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.persistence.entity.Course;

/** Service interface for managing courses. */
public interface CourseService {

  /**
   * Retrieves a course by its ID.
   *
   * @param id The ID of the course to retrieve.
   * @return The course with the specified ID.
   */
  Course getCourseById(String id);

  /**
   * Creates a new course.
   *
   * @param course The course object to create.
   * @return The created course.
   */
  Course createCourse(Course course);

  /**
   * Updates an existing course.
   *
   * @param courseDto The course DTO containing the updated course data.
   * @return The updated course.
   */
  Course updateCourse(CourseDto courseDto);

  /**
   * Deletes a course by its ID.
   *
   * @param id The ID of the course to delete.
   */
  void deleteCourseById(String id);
}
