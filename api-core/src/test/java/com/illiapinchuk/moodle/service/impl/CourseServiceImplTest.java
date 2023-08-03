package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.common.TestConstants;
import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.exception.CourseNotFoundException;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import com.illiapinchuk.moodle.service.TaskService;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

  @Mock private CourseRepository courseRepository;

  @Mock private CourseMapper courseMapper;

  @Mock private CourseValidator courseValidator;

  @Mock private TaskService taskService;

  @InjectMocks private CourseServiceImpl courseService;

  @Test
  void getCourseById_ExistingCourseId_ReturnsCourse() {
    when(courseRepository.findById(TestConstants.CourseConstants.VALID_COURSE_ID))
        .thenReturn(Optional.of(TestConstants.CourseConstants.VALID_COURSE));
    when(taskService.getTasksByCourseId(TestConstants.CourseConstants.VALID_COURSE_ID))
        .thenReturn(Collections.emptyList());

    final var actualCourse =
        courseService.getCourseById(TestConstants.CourseConstants.VALID_COURSE_ID);

    assertSame(TestConstants.CourseConstants.VALID_COURSE, actualCourse);
    assertEquals(TestConstants.CourseConstants.VALID_COURSE_ID, actualCourse.getId());
    assertEquals(0, actualCourse.getTasks().size());

    verify(courseRepository, times(1)).findById(TestConstants.CourseConstants.VALID_COURSE_ID);
    verify(taskService, times(1)).getTasksByCourseId(TestConstants.CourseConstants.VALID_COURSE_ID);
  }

  @Test
  void getCourseById_NonExistingCourseId_ThrowsCourseNotFoundException() {
    when(courseRepository.findById(TestConstants.CourseConstants.INVALID_COURSE_ID))
        .thenReturn(Optional.empty());

    assertThrows(
        CourseNotFoundException.class,
        () -> courseService.getCourseById(TestConstants.CourseConstants.INVALID_COURSE_ID));

    verify(courseRepository, times(1)).findById(TestConstants.CourseConstants.INVALID_COURSE_ID);
    verifyNoInteractions(taskService);
  }

  @Test
  void createCourse_ValidCourse_CreatesAndReturnsCourse() {
    when(courseValidator.isAuthorsExistsInDbByIds(
            TestConstants.CourseConstants.VALID_COURSE.getAuthorIds()))
        .thenReturn(true);
    when(courseRepository.save(TestConstants.CourseConstants.VALID_COURSE))
        .thenReturn(TestConstants.CourseConstants.VALID_COURSE);

    final var createdCourse =
        courseService.createCourse(TestConstants.CourseConstants.VALID_COURSE);

    assertSame(TestConstants.CourseConstants.VALID_COURSE, createdCourse);

    verify(courseValidator, times(1))
        .isAuthorsExistsInDbByIds(TestConstants.CourseConstants.VALID_COURSE.getAuthorIds());
    verify(courseRepository, times(1)).save(TestConstants.CourseConstants.VALID_COURSE);
  }

  @Test
  void createCourse_InvalidAuthorIds_ThrowsUserNotFoundException() {
    when(courseValidator.isAuthorsExistsInDbByIds(
            TestConstants.CourseConstants.VALID_COURSE.getAuthorIds()))
        .thenReturn(false);

    assertThrows(
        UserNotFoundException.class,
        () -> courseService.createCourse(TestConstants.CourseConstants.VALID_COURSE));

    verify(courseValidator, times(1))
        .isAuthorsExistsInDbByIds(TestConstants.CourseConstants.VALID_COURSE.getAuthorIds());
    verifyNoInteractions(courseRepository);
  }

  @Test
  void updateCourse_ExistingCourseDto_UpdatesAndReturnsCourse() {
    when(courseValidator.isCourseExistsInDbById(
            TestConstants.CourseConstants.VALID_COURSE_DTO.getId()))
        .thenReturn(true);
    when(courseRepository.findById(TestConstants.CourseConstants.VALID_COURSE_DTO.getId()))
        .thenReturn(Optional.of(TestConstants.CourseConstants.VALID_COURSE));
    when(courseRepository.save(TestConstants.CourseConstants.VALID_COURSE))
        .thenReturn(TestConstants.CourseConstants.VALID_COURSE);

    final var updatedCourse =
        courseService.updateCourse(TestConstants.CourseConstants.VALID_COURSE_DTO);

    assertSame(TestConstants.CourseConstants.VALID_COURSE, updatedCourse);

    verify(courseValidator, times(1))
        .isCourseExistsInDbById(TestConstants.CourseConstants.VALID_COURSE_DTO.getId());
    verify(courseMapper, times(1))
        .updateCourse(
            TestConstants.CourseConstants.VALID_COURSE,
            TestConstants.CourseConstants.VALID_COURSE_DTO);
    verify(courseRepository, times(1)).save(TestConstants.CourseConstants.VALID_COURSE);
  }

  @Test
  void updateCourse_NonExistingCourseDto_ThrowsCourseNotFoundException() {
    when(courseValidator.isCourseExistsInDbById(
            TestConstants.CourseConstants.INVALID_COURSE_DTO.getId()))
        .thenReturn(false);

    assertThrows(
        CourseNotFoundException.class,
        () -> courseService.updateCourse(TestConstants.CourseConstants.INVALID_COURSE_DTO));

    verify(courseValidator, times(1))
        .isCourseExistsInDbById(TestConstants.CourseConstants.INVALID_COURSE_DTO.getId());
    verifyNoInteractions(courseRepository);
  }

  @Test
  void deleteCourseById_ExistingCourseId_DeletesCourseAndAssociatedTasks() {
    when(courseRepository.findById(TestConstants.CourseConstants.VALID_COURSE_ID))
        .thenReturn(Optional.of(TestConstants.CourseConstants.VALID_COURSE));

    courseService.deleteCourseById(TestConstants.CourseConstants.VALID_COURSE.getId());

    verify(taskService, times(TestConstants.CourseConstants.VALID_COURSE.getTasks().size()))
        .deleteTaskById(anyString());
    verify(courseRepository, times(1))
        .deleteById(TestConstants.CourseConstants.VALID_COURSE.getId());
  }

  @Test
  void deleteCourseById_NonExistingCourseId_ThrowsCourseNotFoundException() {
    when(courseRepository.findById(TestConstants.CourseConstants.INVALID_COURSE_ID))
        .thenReturn(Optional.empty());

    assertThrows(
        CourseNotFoundException.class,
        () -> courseService.deleteCourseById(TestConstants.CourseConstants.INVALID_COURSE_ID));
  }
}
