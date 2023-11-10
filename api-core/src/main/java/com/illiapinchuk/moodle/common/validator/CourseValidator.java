package com.illiapinchuk.moodle.common.validator;

import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import jakarta.annotation.Nonnull;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Validation for course-related information. */
@Component
@RequiredArgsConstructor
public class CourseValidator {

  private final CourseRepository courseRepository;
  private final UserRepository userRepository;

  /**
   * Check if the given course exists in the database.
   *
   * @param id the id to check
   * @return true if the course with this id exists in the database, false otherwise
   */
  public boolean isCourseExistsInDbById(@Nonnull final String id) {
    return courseRepository.existsById(id);
  }

  /**
   * Checks if all the authors with the specified IDs exist in the database.
   *
   * @param authorIds a list of non-blank strings representing author IDs
   * @return true if all authors with the given IDs exist in the database, false otherwise
   */
  public boolean isAuthorsExistsInDbByIds(@Nonnull final Set<Long> authorIds) {
    return authorIds.stream().allMatch(userRepository::existsById);
  }

  /**
   * Checks if a student with the given ID is enrolled in a specific course.
   *
   * @param courseId The ID of the course to check.
   * @param studentId The ID of the student to check for enrollment in the specified course.
   * @return {@code true} if the student with the provided ID is enrolled in the specified course,
   *     {@code false} otherwise.
   */
  public boolean isStudentEnrolledInCourse(
      @Nonnull final String courseId, @Nonnull final Long studentId) {
    return courseRepository.existsByIdAndStudentsContains(courseId, studentId);
  }

  /**
   * Determines if a student is enrolled in a course containing a specific task.
   *
   * @param taskId The ID of the task.
   * @param studentId The ID of the student to check.
   * @return true if the student is enrolled in the course with the given task; false otherwise.
   */
  public boolean isStudentEnrolledInCourseWithTask(
      @Nonnull final String taskId, @Nonnull final Long studentId) {
    return courseRepository.existsByTasksIdAndStudentsContains(taskId, studentId);
  }
}
