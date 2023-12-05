package com.illiapinchuk.moodle.service.impl.crud;

import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.CourseAlreadyExistsException;
import com.illiapinchuk.moodle.exception.CourseNotFoundException;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import com.illiapinchuk.moodle.service.crud.CourseCRUDService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of {@link CourseCRUDService} interface. */
@Service
@RequiredArgsConstructor
@Slf4j
public class CourseCRUDServiceImpl implements CourseCRUDService {

  private final CourseRepository courseRepository;

  private final CourseMapper courseMapper;

  private final CourseValidator courseValidator;
  private final UserValidator userValidator;

  @Override
  @Transactional(readOnly = true)
  public Course getCourseById(@Nonnull final String courseId) {
    userValidator.checkIfUserHasAccessToCourse(courseId);

    return courseRepository
        .findById(courseId)
        .orElseThrow(
            () ->
                new CourseNotFoundException(
                    String.format("Course with id: %s not found", courseId)));
  }

  @Override
  @Transactional
  public Course createCourse(@Nonnull final Course course) {
    if (course.getId() != null && courseValidator.isCourseExistsInDbById(course.getId()))
      throw new CourseAlreadyExistsException(
          String.format("Course with id: %s already exists", course.getId()));

    if (!courseValidator.isAuthorsExistsInDbByIds(course.getAuthorIds()))
      throw new UserNotFoundException("One of the authors not exists.");
    return courseRepository.save(course);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Course updateCourseFromDto(@Nonnull final CourseDto courseDto) {
    final var courseId = courseDto.getId();
    if (!courseValidator.isCourseExistsInDbById(courseId)) {
      throw new CourseNotFoundException(String.format("Course with id: %s not found", courseId));
    }
    final var course = getCourseById(courseId);
    courseMapper.updateCourse(course, courseDto);

    return courseRepository.save(course);
  }

  @Override
  public Course updateCourse(@Nonnull final Course course) {
    return courseRepository.save(course);
  }

  @Override
  public void deleteCourseById(@Nonnull final String id) {
    courseRepository.deleteById(id);
  }
}
