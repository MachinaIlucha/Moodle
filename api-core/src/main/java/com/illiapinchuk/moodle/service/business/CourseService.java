package com.illiapinchuk.moodle.service.business;

import com.illiapinchuk.moodle.persistence.entity.Course;

import java.util.List;

/** Service interface for managing courses. */
public interface CourseService {

  /**
   * Retrieves courses for a given user.
   *
   * @param userId the id of the user whose courses are to be retrieved
   * @return a list of courses
   */
  List<Course> getCoursesForUser(Long userId);

  /**
   * Adds one or more students to a course with the specified ID.
   *
   * @param id The ID of the course to which students will be added.
   * @param studentIds a list of student ids to add to the course
   */
  void addStudentsToCourse(String id, List<Long> studentIds);

  /**
   * Deletes a student from a course based on the provided student ID.
   *
   * @param id The ID of the course from which the student should be deleted.
   * @param studentIds The ID of the student to be removed from the course.
   */
  void removeStudentFromCourse(String id, Long studentIds);

  /**
   * Retrieves the course ID associated with a given task ID.
   *
   * @param taskId The unique identifier of the task for which the course ID is needed.
   * @return The course ID associated with the specified task ID, or null if not found.
   */
  String getCourseIdByTaskId(String taskId);
}
