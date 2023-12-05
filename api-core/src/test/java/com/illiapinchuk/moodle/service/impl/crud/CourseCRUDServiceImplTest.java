package com.illiapinchuk.moodle.service.impl.crud;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.CourseAlreadyExistsException;
import com.illiapinchuk.moodle.exception.CourseNotFoundException;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.INVALID_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_WITHOUT_TASKS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseCRUDServiceImplTest {
  @Mock private CourseRepository courseRepository;

  @Mock private CourseMapper courseMapper;

  @Mock private CourseValidator courseValidator;

  @Mock private UserValidator userValidator;

  @InjectMocks private CourseCRUDServiceImpl courseCRUDService;

  @Test
  void getCourseById_ExistingCourseId_ReturnsCourse() {
    when(courseRepository.findById(VALID_COURSE_ID))
            .thenReturn(Optional.of(VALID_COURSE_WITHOUT_TASKS));

    final var actualCourse = courseCRUDService.getCourseById(VALID_COURSE_ID);

    assertSame(VALID_COURSE_WITHOUT_TASKS, actualCourse);
    assertEquals(VALID_COURSE_ID, actualCourse.getId());

    verify(courseRepository, times(1)).findById(VALID_COURSE_ID);
  }

  @Test
  void getCourseById_NonExistingCourseId_ThrowsCourseNotFoundException() {
    assertThrows(
            CourseNotFoundException.class, () -> courseCRUDService.getCourseById(INVALID_COURSE_ID));

    verify(courseRepository, times(1)).findById(INVALID_COURSE_ID);
  }

  @Test
  void getCourseById_UserNotEnrolledAndNoRulingRole_ThrowsUserDontHaveAccessToResource() {
    doThrow(UserDontHaveAccessToResource.class)
            .when(userValidator)
            .checkIfUserHasAccessToCourse(VALID_COURSE_ID);

    assertThrows(
            UserDontHaveAccessToResource.class,
            () -> courseCRUDService.getCourseById(VALID_COURSE_ID));

    verifyNoInteractions(courseRepository);
  }

  @Test
  void getCourseById_UserEnrolled_ReturnsCourse() {
    when(courseRepository.findById(VALID_COURSE_ID))
            .thenReturn(Optional.of(VALID_COURSE_WITHOUT_TASKS));

    final var actualCourse = courseCRUDService.getCourseById(VALID_COURSE_ID);

    assertSame(VALID_COURSE_WITHOUT_TASKS, actualCourse);
  }

  @Test
  void createCourse_CourseAlreadyExists_ThrowsCourseAlreadyExists() {
    when(courseValidator.isCourseExistsInDbById(VALID_COURSE_ID)).thenReturn(true);

    assertThrows(
            CourseAlreadyExistsException.class, () -> courseCRUDService.createCourse(VALID_COURSE));

    verify(courseValidator, times(1)).isCourseExistsInDbById(VALID_COURSE_ID);
    verifyNoInteractions(courseRepository);
  }

  @Test
  void createCourse_InvalidAuthorIds_ThrowsUserNotFoundException() {
    when(courseValidator.isAuthorsExistsInDbByIds(
            TestConstants.CourseConstants.VALID_COURSE.getAuthorIds()))
            .thenReturn(false);

    assertThrows(
            UserNotFoundException.class,
            () -> courseCRUDService.createCourse(TestConstants.CourseConstants.VALID_COURSE));

    verify(courseValidator, times(1))
            .isAuthorsExistsInDbByIds(TestConstants.CourseConstants.VALID_COURSE.getAuthorIds());
    verifyNoInteractions(courseRepository);
  }

  @Test
  void updateCourse_ExistingCourseDto_UpdatesAndReturnsCourse() {
    when(courseValidator.isCourseExistsInDbById(VALID_COURSE_DTO.getId())).thenReturn(true);
    when(courseRepository.findById(VALID_COURSE_DTO.getId())).thenReturn(Optional.of(VALID_COURSE));
    when(courseRepository.save(VALID_COURSE)).thenReturn(VALID_COURSE);

    final var updatedCourse = courseCRUDService.updateCourseFromDto(VALID_COURSE_DTO);

    assertSame(VALID_COURSE, updatedCourse);

    verify(courseValidator, times(1)).isCourseExistsInDbById(VALID_COURSE_DTO.getId());
    verify(courseMapper, times(1))
            .updateCourse(TestConstants.CourseConstants.VALID_COURSE, VALID_COURSE_DTO);
    verify(courseRepository, times(1)).save(TestConstants.CourseConstants.VALID_COURSE);
  }

  @Test
  void updateCourse_NonExistingCourseDto_ThrowsCourseNotFoundException() {
    when(courseValidator.isCourseExistsInDbById(
            TestConstants.CourseConstants.INVALID_COURSE_DTO.getId()))
            .thenReturn(false);

    assertThrows(
            CourseNotFoundException.class,
            () -> courseCRUDService.updateCourseFromDto(TestConstants.CourseConstants.INVALID_COURSE_DTO));

    verify(courseValidator, times(1))
            .isCourseExistsInDbById(TestConstants.CourseConstants.INVALID_COURSE_DTO.getId());
    verifyNoInteractions(courseRepository);
  }
}
