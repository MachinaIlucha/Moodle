package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.CourseNotFoundException;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import com.illiapinchuk.moodle.service.TaskService;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.INVALID_COURSE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
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

  private static MockedStatic<UserPermissionService> mockedUserPermissionService;

  @BeforeAll
  static void setupUserPermissionServiceMocks() {
    if (mockedUserPermissionService != null) {
      mockedUserPermissionService.close();
    }
    mockedUserPermissionService = mockStatic(UserPermissionService.class);
    mockedUserPermissionService
        .when(UserPermissionService::getJwtUser)
        .thenReturn(TestConstants.UserConstants.ADMIN_JWT_USER);
    mockedUserPermissionService.when(UserPermissionService::hasAnyRulingRole).thenReturn(true);
  }

  @AfterAll
  static void closeUserPermissionServiceMocks() {
    mockedUserPermissionService.close();
  }

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
    when(courseRepository.findById(INVALID_COURSE_ID)).thenReturn(Optional.empty());
    when(courseValidator.isStudentEnrolledInCourse(
            INVALID_COURSE_ID, TestConstants.UserConstants.ADMIN_JWT_USER.getId()))
        .thenReturn(true);

    assertThrows(
        CourseNotFoundException.class, () -> courseService.getCourseById(INVALID_COURSE_ID));

    verify(courseRepository, times(1)).findById(INVALID_COURSE_ID);
    verifyNoInteractions(taskService);
  }

  @Test
  void getCourseById_UserNotEnrolledAndNoRulingRole_ThrowsUserDontHaveAccessToResource() {
    when(courseValidator.isStudentEnrolledInCourse(anyString(), any())).thenReturn(false);
    when(UserPermissionService.hasAnyRulingRole()).thenReturn(false);

    assertThrows(
        UserDontHaveAccessToResource.class,
        () -> courseService.getCourseById(TestConstants.CourseConstants.VALID_COURSE_ID));

    verifyNoInteractions(courseRepository, taskService);
  }

  @Test
  void getCourseById_UserEnrolled_ReturnsCourse() {
    when(courseValidator.isStudentEnrolledInCourse(anyString(), any())).thenReturn(true);

    when(courseRepository.findById(TestConstants.CourseConstants.VALID_COURSE_ID))
        .thenReturn(Optional.of(TestConstants.CourseConstants.VALID_COURSE));
    when(taskService.getTasksByCourseId(TestConstants.CourseConstants.VALID_COURSE_ID))
        .thenReturn(Collections.emptyList());

    final var actualCourse =
        courseService.getCourseById(TestConstants.CourseConstants.VALID_COURSE_ID);

    assertSame(TestConstants.CourseConstants.VALID_COURSE, actualCourse);
  }

  @Test
  void getCourseById_UserHasRulingRole_ReturnsCourse() {
    when(courseValidator.isStudentEnrolledInCourse(anyString(), any()))
        .thenReturn(false); // Not enrolled
    when(UserPermissionService.hasAnyRulingRole()).thenReturn(true);

    when(courseRepository.findById(TestConstants.CourseConstants.VALID_COURSE_ID))
        .thenReturn(Optional.of(TestConstants.CourseConstants.VALID_COURSE));
    when(taskService.getTasksByCourseId(TestConstants.CourseConstants.VALID_COURSE_ID))
        .thenReturn(Collections.emptyList());

    final var actualCourse =
        courseService.getCourseById(TestConstants.CourseConstants.VALID_COURSE_ID);

    assertSame(TestConstants.CourseConstants.VALID_COURSE, actualCourse);
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
    when(courseRepository.findById(INVALID_COURSE_ID)).thenReturn(Optional.empty());

    assertThrows(
        CourseNotFoundException.class, () -> courseService.deleteCourseById(INVALID_COURSE_ID));
  }
}
