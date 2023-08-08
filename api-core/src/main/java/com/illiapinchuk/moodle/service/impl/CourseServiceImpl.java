package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Implementation of {@link CourseService} interface. */
@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;
  private final CourseValidator courseValidator;
  private final TaskService taskService;

  @Override
  public Course getCourseById(@Nonnull final String courseId) {
    final var userId = UserPermissionService.getJwtUser().getId();

    if (!courseValidator.isStudentEnrolledInCourse(courseId, userId)
        && !UserPermissionService.hasAnyRulingRole()) {
      log.error("User with id - {} trying to access course with id - {}", userId, courseId);
      throw new UserDontHaveAccessToResource("User doesn't have access to this course.");
    }

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
}
