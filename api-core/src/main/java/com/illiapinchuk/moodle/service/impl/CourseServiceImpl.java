package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.exception.CourseNotFoundException;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import com.illiapinchuk.moodle.service.CourseService;
import com.illiapinchuk.moodle.service.TaskService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Implementation of {@link CourseService} interface. */
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;
  private final CourseValidator courseValidator;
  private final TaskService taskService;

  @Override
  public Course getCourseById(@Nonnull final String id) {
    final var course =
        courseRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new CourseNotFoundException(String.format("Course with id: %s not found", id)));
    course.setTasks(taskService.getTasksByCourseId(id));
    return course;
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
