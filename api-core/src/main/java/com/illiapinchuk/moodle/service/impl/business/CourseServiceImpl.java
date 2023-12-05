package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.CourseNotFoundException;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.entity.RoleName;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import com.illiapinchuk.moodle.service.business.CourseService;
import com.illiapinchuk.moodle.service.business.UserService;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/** Implementation of {@link CourseService} interface. */
@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;
  private final UserValidator userValidator;
  private final UserService userService;

  @Override
  @Transactional(readOnly = true)
  public List<Course> getCoursesForUser(@Nonnull final Long userId) {
    if (!userValidator.isUserExistInDbById(userId)) {
      throw new UserNotFoundException(String.format("User with id: %s not found", userId));
    }

    final var userRoles = userService.getUserById(userId).getRoles();

    final var courses = new HashSet<Course>();

    boolean isAdminOrDeveloper = false;

    for (RoleName role : userRoles) {
      switch (role) {
        case USER -> courses.addAll(courseRepository.findByStudentsContains(userId));
        case TEACHER -> courses.addAll(courseRepository.findByAuthorIdsContains(userId));
        case ADMIN, DEVELOPER -> isAdminOrDeveloper = true;
        default -> throw new IllegalArgumentException("Unhandled user role: " + role);
      }
    }

    // Add courses for Admin/Developer roles only if required
    // If the user is an Admin or Developer, add courses where they are a student or author
    if (isAdminOrDeveloper) {
      courses.addAll(courseRepository.findByStudentsContains(userId));
      courses.addAll(courseRepository.findByAuthorIdsContains(userId));
    }

    return new ArrayList<>(courses);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void addStudentsToCourse(
      @Nonnull final String courseId, @NotEmpty final List<Long> studentIds) {
    userValidator.checkIfUserHasAccessToCourse(courseId);

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
  @Transactional(rollbackFor = Exception.class)
  public void removeStudentFromCourse(
      @Nonnull final String courseId, @Nonnull final Long studentIds) {
    userValidator.checkIfUserHasAccessToCourse(courseId);

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
  public String getCourseIdByTaskId(@Nonnull final String taskId) {
    return courseRepository.findCourseIdByTasksId(taskId);
  }
}
