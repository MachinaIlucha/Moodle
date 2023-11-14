package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.CourseNotFoundException;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import com.illiapinchuk.moodle.service.CourseService;
import com.illiapinchuk.moodle.service.TaskService;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/** Implementation of {@link CourseService} interface. */
@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;
  private final CourseValidator courseValidator;
  private final TaskService taskService;
  private final UserValidator userValidator;

  @Override
  public void addStudentsToCourse(
      @Nonnull final String courseId, @NotEmpty final List<Long> studentIds) {
    checkIfUserHasAccessToCourse(courseId);

    final var course =
        courseRepository
            .findById(courseId)
            .orElseThrow(
                () ->
                    new CourseNotFoundException(
                        String.format("Course with id: %s not found", courseId)));

    // Retrieve all students at once to minimize database calls
    final var nonExistingStudents =
        studentIds.stream()
            .filter(studentId -> !userValidator.isUserExistInDbById(studentId))
            .toList();

    // If there are non-existing students, throw an exception
    if (!nonExistingStudents.isEmpty()) {
      final var nonExistingStudentIds =
          nonExistingStudents.stream().map(String::valueOf).collect(Collectors.joining(", "));
      final var errorMessage =
          String.format("Students with ids: %s not found", nonExistingStudentIds);
      log.error(errorMessage);
      throw new UserNotFoundException(errorMessage);
    }

    boolean changed = course.getStudents().addAll(studentIds);
    if (changed) {
      courseRepository.save(course); // Save only if there was a change
    }
  }

  @Override
  public void removeStudentFromCourse(
      @Nonnull final String courseId, @Nonnull final Long studentIds) {
    checkIfUserHasAccessToCourse(courseId);

    final var course =
        courseRepository
            .findById(courseId)
            .orElseThrow(
                () ->
                    new CourseNotFoundException(
                        String.format("Course with id: %s not found", courseId)));

    boolean changed = course.getStudents().remove(studentIds);
    if (changed) {
      courseRepository.save(course); // Save only if there was a change
    }
  }

  @Override
  public Course getCourseById(@Nonnull final String courseId) {
    checkIfUserHasAccessToCourse(courseId);

    return courseRepository
        .findById(courseId)
        .map(
            course -> {
              course.setTasks(taskService.getTasksByCourseId(courseId));
              return course;
            })
        .orElseThrow(
            () ->
                new CourseNotFoundException(
                    String.format("Course with id: %s not found", courseId)));
  }

  @Override
  public Course createCourse(@Nonnull final Course course) {
    if (!courseValidator.isAuthorsExistsInDbByIds(course.getAuthorIds()))
      throw new UserNotFoundException("One of the authors not exists.");
    return courseRepository.save(course);
  }

  @Override
  public Course updateCourse(@Nonnull final CourseDto courseDto) {
    final var courseId = courseDto.getId();
    if (!courseValidator.isCourseExistsInDbById(courseId)) {
      throw new CourseNotFoundException(String.format("Course with id: %s not found", courseId));
    }
    final var course = getCourseById(courseId);
    courseMapper.updateCourse(course, courseDto);

    return courseRepository.save(course);
  }

  @Override
  public void deleteCourseById(@Nonnull final String id) {
    final var course = getCourseById(id);

    course.getTasks().forEach(task -> taskService.deleteTaskById(task.getId()));

    courseRepository.deleteById(id);
  }

  /**
   * Checks if the user identified by their JWT token has access to a specific course.
   *
   * @param courseId The ID of the course to check access for.
   * @throws UserDontHaveAccessToResource If the user doesn't have access to the specified course.
   */
  private void checkIfUserHasAccessToCourse(@Nonnull final String courseId) {
    final var userId = UserPermissionService.getJwtUser().getId();

    if (!courseValidator.isStudentEnrolledInCourse(courseId, userId)
        && !UserPermissionService.hasAnyRulingRole()) {
      log.error("User with id - {} trying to access course with id - {}", userId, courseId);
      throw new UserDontHaveAccessToResource("User doesn't have access to this course.");
    }
  }
}
