package com.illiapinchuk.moodle.service.crud;

import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.persistence.entity.Course;

/** Provides CRUD (Create, Read, Update, Delete) service operations for {@link Course} entities. */
public interface CourseCRUDService {

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
  Course updateCourseFromDto(CourseDto courseDto);

  /**
   * Updates an existing course.
   *
   * @param course The course containing the updated course data.
   * @return The updated course.
   */
  Course updateCourse(Course course);

  /**
   * Deletes a course by its ID.
   *
   * @param id The ID of the course to delete.
   */
  void deleteCourseById(String id);
}
