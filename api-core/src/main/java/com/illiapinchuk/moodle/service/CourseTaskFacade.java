package com.illiapinchuk.moodle.service;

import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.model.dto.TaskDto;

/**
 * The {@code CourseTaskFacade} interface defines methods for retrieving information related to
 * courses and their associated tasks.
 */
public interface CourseTaskFacade {

  /**
   * Retrieves a {@link CourseDto} object that represents a course along with its tasks based on the
   * specified course ID.
   *
   * @param courseId The unique identifier of the course.
   * @return A {@link CourseDto} containing information about the course and its tasks.
   */
  CourseDto getCourseWithTasks(String courseId);

  /**
   * Deletes a course and all its associated tasks based on the course ID. This operation is
   * irreversible and will remove all data related to the course and its tasks.
   *
   * @param courseId The unique identifier of the course to be deleted.
   */
  void deleteCourseByIdWithTasks(String courseId);

  /**
   * Creates a new task and associates it with a specified course. The course association is
   * determined based on the details provided within the {@link TaskDto}.
   *
   * @param taskDto The {@link TaskDto} object containing information about the task to be created,
   *     including the course it should be associated with.
   * @return A {@link TaskDto} object representing the newly created task with its associated
   *     course. Returns {@code null} if the task creation fails or if the specified course does not
   *     exist.
   */
  TaskDto createTaskWithCourse(TaskDto taskDto);

  /**
   * Adds a task to a course.
   *
   * @param courseId The ID of the course to which the task should be added.
   * @param taskId The ID of the task to be added to the course.
   */
  void addTaskToCourse(String courseId, String taskId);
}
