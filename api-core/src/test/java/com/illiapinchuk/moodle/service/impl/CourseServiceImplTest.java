package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.CourseNotFoundException;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import com.illiapinchuk.moodle.service.TaskService;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.illiapinchuk.moodle.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.INVALID_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_WITH_STUDENTS;
import static com.illiapinchuk.moodle.common.TestConstants.UserConstants.INVALID_USER_ID;
import static com.illiapinchuk.moodle.common.TestConstants.UserConstants.USER_ID;
import static com.illiapinchuk.moodle.common.TestConstants.UserConstants.VALID_ADMIN_USER_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

  @Mock private CourseRepository courseRepository;

  @Mock private CourseMapper courseMapper;

  @Mock private CourseValidator courseValidator;
  @Mock private UserValidator userValidator;

  @Mock private TaskService taskService;
  @Mock private UserService userService;

  @InjectMocks private CourseServiceImpl courseService;

  private static MockedStatic<UserPermissionService> mockedUserPermissionService;

  @BeforeAll
  static void setupUserPermissionServiceMocks() {
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
  void getCoursesForUser_UserNotFound() {
    when(userValidator.isUserExistInDbById(INVALID_USER_ID)).thenReturn(false);

    assertThrows(
        UserNotFoundException.class, () -> courseService.getCoursesForUser(INVALID_USER_ID));
  }

  @Test
  void getCoursesForUser_UserWithMultipleRoles() {
    when(userValidator.isUserExistInDbById(USER_ID)).thenReturn(true);
    when(userService.getUserById(USER_ID)).thenReturn(VALID_ADMIN_USER_2);
    when(courseRepository.findByStudentsContains(USER_ID))
        .thenReturn(List.of(VALID_COURSE_WITH_STUDENTS));
    when(courseRepository.findByAuthorIdsContains(USER_ID))
        .thenReturn(List.of(VALID_COURSE_WITH_STUDENTS));

    final var courses = courseService.getCoursesForUser(USER_ID);

    verify(courseRepository, times(1)).findByStudentsContains(USER_ID);
    verify(courseRepository, times(1)).findByAuthorIdsContains(USER_ID);
    assertEquals(1, courses.size());
  }

  @Test
  void removeStudentFromCourse_SuccessfulRemoval() {
    final var mockCourse = new Course();
    mockCourse.setStudents(new HashSet<>(List.of(USER_ID)));
    when(courseRepository.findById(VALID_COURSE_ID)).thenReturn(Optional.of(mockCourse));

    courseService.removeStudentFromCourse(VALID_COURSE_ID, USER_ID);

    assertFalse(mockCourse.getStudents().contains(USER_ID));
    verify(courseRepository).save(mockCourse);
  }

  @Test
  void removeStudentFromCourse_StudentNotInCourse() {
    final var mockCourse = new Course();
    mockCourse.setStudents(new HashSet<>());
    when(courseRepository.findById(VALID_COURSE_ID)).thenReturn(Optional.of(mockCourse));

    courseService.removeStudentFromCourse(VALID_COURSE_ID, USER_ID);

    assertFalse(mockCourse.getStudents().contains(USER_ID));
    verify(courseRepository, never()).save(mockCourse);
  }

  @Test
  void removeStudentFromCourse_CourseNotFound() {
    when(courseRepository.findById(INVALID_COURSE_ID)).thenReturn(Optional.empty());

    assertThrows(
        CourseNotFoundException.class,
        () -> courseService.removeStudentFromCourse(INVALID_COURSE_ID, USER_ID));
  }

  @Test
  void addStudentsToCourse_ValidCourseIdAndStudentIds_AddsStudentsToCourse() {
    when(userValidator.isUserExistInDbById(anyLong())).thenReturn(true);
    when(courseRepository.findById(anyString()))
        .thenReturn(Optional.of(VALID_COURSE_WITH_STUDENTS));

    courseService.addStudentsToCourse(VALID_COURSE_ID, List.of(5L, 6L));

    verify(courseRepository, times(1)).save(VALID_COURSE_WITH_STUDENTS);
  }

  @Test
  void addStudentsToCourse_InvalidCourseId_ThrowsCourseNotFoundException() {
    assertThrows(
        CourseNotFoundException.class,
        () -> courseService.addStudentsToCourse(INVALID_COURSE_ID, Collections.emptyList()));

    verifyNoInteractions(taskService);
  }

  @Test
  void addStudentsToCourse_StudentsAlreadyEnrolled_DoesNotSaveCourse() {
    when(userValidator.isUserExistInDbById(anyLong())).thenReturn(true);
    when(courseRepository.findById(anyString()))
        .thenReturn(Optional.of(VALID_COURSE_WITH_STUDENTS));
    when(UserPermissionService.hasAnyRulingRole()).thenReturn(true);

    courseService.addStudentsToCourse(VALID_COURSE_ID, List.of(8L, 9L));

    verify(courseRepository, times(0)).save(TestConstants.CourseConstants.VALID_COURSE);
  }

  @Test
  void addStudentsToCourse_InvalidStudentIds_ThrowsUserNotFoundException() {
    when(courseRepository.findById(VALID_COURSE_ID)).thenReturn(Optional.of(VALID_COURSE));
    when(courseValidator.isStudentEnrolledInCourse(anyString(), anyLong())).thenReturn(true);

    assertThrows(
        UserNotFoundException.class,
        () -> courseService.addStudentsToCourse(VALID_COURSE_ID, List.of(5L, 6L)));

    verify(courseRepository, times(1)).findById(VALID_COURSE_ID);
  }

  @Test
  void
      addStudentsToCourse_EmptyListOfStudentIds_DoesNotThrowExceptionAndDoesNotCallCourseRepositorySave() {
    when(courseRepository.findById(anyString()))
        .thenReturn(Optional.of(VALID_COURSE_WITH_STUDENTS));

    courseService.addStudentsToCourse(VALID_COURSE_ID, Collections.emptyList());

    verify(courseRepository, times(0)).save(VALID_COURSE_WITH_STUDENTS);
  }

  @Test
  void addStudentsToCourse_NullCourseId_ThrowsCourseNotFoundException() {
    assertThrows(
        CourseNotFoundException.class,
        () -> courseService.addStudentsToCourse(null, Collections.emptyList()));

    verify(courseRepository, times(1)).findById(null);
  }

  @Test
  void addStudentsToCourse_WithDuplicateStudentIds_IgnoresDuplicates() {
    final List<Long> studentIdsWithDuplicates = List.of(1L, 2L, 2L, 3L); // Duplicate ID '2L'
    final Course courseBeforeUpdate = new Course();
    courseBeforeUpdate.setStudents(new HashSet<>(List.of(1L, 3L))); // Existing students

    when(userValidator.isUserExistInDbById(anyLong())).thenReturn(true);
    when(courseRepository.findById(VALID_COURSE_ID)).thenReturn(Optional.of(courseBeforeUpdate));

    courseService.addStudentsToCourse(VALID_COURSE_ID, studentIdsWithDuplicates);

    ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);
    verify(courseRepository).save(courseArgumentCaptor.capture());
    Set<Long> savedStudentIds = courseArgumentCaptor.getValue().getStudents();

    assertThat(savedStudentIds).hasSize(3); // Should only have 3 unique IDs
    assertThat(savedStudentIds).containsExactlyInAnyOrder(1L, 2L, 3L); // No duplicates
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
    when(courseValidator.isCourseExistsInDbById(VALID_COURSE_DTO.getId())).thenReturn(true);
    when(courseRepository.findById(VALID_COURSE_DTO.getId())).thenReturn(Optional.of(VALID_COURSE));
    when(courseRepository.save(VALID_COURSE)).thenReturn(VALID_COURSE);

    final var updatedCourse = courseService.updateCourse(VALID_COURSE_DTO);

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
        () -> courseService.updateCourse(TestConstants.CourseConstants.INVALID_COURSE_DTO));

    verify(courseValidator, times(1))
        .isCourseExistsInDbById(TestConstants.CourseConstants.INVALID_COURSE_DTO.getId());
    verifyNoInteractions(courseRepository);
  }

  @Test
  void deleteCourseById_ExistingCourseId_DeletesCourseAndAssociatedTasks() {
    when(courseRepository.findById(VALID_COURSE_ID)).thenReturn(Optional.of(VALID_COURSE));

    courseService.deleteCourseById(VALID_COURSE.getId());

    verify(taskService, times(VALID_COURSE.getTasks().size())).deleteTaskById(anyString());
    verify(courseRepository, times(1)).deleteById(VALID_COURSE.getId());
  }

  @Test
  void deleteCourseById_NonExistingCourseId_ThrowsCourseNotFoundException() {
    when(courseRepository.findById(INVALID_COURSE_ID)).thenReturn(Optional.empty());

    assertThrows(
        CourseNotFoundException.class, () -> courseService.deleteCourseById(INVALID_COURSE_ID));
  }
}
