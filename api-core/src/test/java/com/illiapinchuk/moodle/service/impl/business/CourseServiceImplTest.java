package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.CourseNotFoundException;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import com.illiapinchuk.moodle.service.business.TaskService;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.illiapinchuk.moodle.service.business.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.INVALID_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_WITH_STUDENTS;
import static com.illiapinchuk.moodle.common.TestConstants.UserConstants.INVALID_USER_ID;
import static com.illiapinchuk.moodle.common.TestConstants.UserConstants.USER_ID;
import static com.illiapinchuk.moodle.common.TestConstants.UserConstants.VALID_ADMIN_USER_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

    courseService.addStudentsToCourse(VALID_COURSE_ID, List.of(8L, 9L));

    verify(courseRepository, times(0)).save(TestConstants.CourseConstants.VALID_COURSE);
  }

  @Test
  void addStudentsToCourse_InvalidStudentIds_ThrowsUserNotFoundException() {
    when(courseRepository.findById(VALID_COURSE_ID)).thenReturn(Optional.of(VALID_COURSE));

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
}
