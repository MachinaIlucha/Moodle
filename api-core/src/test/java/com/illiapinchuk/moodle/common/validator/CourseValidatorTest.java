package com.illiapinchuk.moodle.common.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
class CourseValidatorTest {

  @Mock private CourseRepository courseRepository;

  @Mock private UserRepository userRepository;

  @InjectMocks private CourseValidator courseValidator;

  @Test
  void testIsCourseExistsInDbById_ExistingCourseId_ReturnsTrue() {
    when(courseRepository.existsById(TestConstants.CourseConstants.VALID_COURSE_ID))
        .thenReturn(true);

    boolean result =
        courseValidator.isCourseExistsInDbById(TestConstants.CourseConstants.VALID_COURSE_ID);

    assertTrue(result);
  }

  @Test
  void testIsCourseExistsInDbById_NonExistingCourseId_ReturnsFalse() {
    when(courseRepository.existsById(TestConstants.CourseConstants.VALID_COURSE_ID))
        .thenReturn(false);

    boolean result =
        courseValidator.isCourseExistsInDbById(TestConstants.CourseConstants.VALID_COURSE_ID);

    assertFalse(result);
  }

  @Test
  void testIsAuthorsExistsInDbByIds_AllAuthorsExist_ReturnsTrue() {
    Set<Long> userSet = TestConstants.UserConstants.SET_OF_USER_IDS;

    when(userRepository.existsById(userSet.stream().findFirst().orElse(null))).thenReturn(true);
    when(userRepository.existsById(userSet.stream().skip(1).findFirst().orElse(null))).thenReturn(true);
    when(userRepository.existsById(userSet.stream().skip(2).findFirst().orElse(null))).thenReturn(true);

    boolean result = courseValidator.isAuthorsExistsInDbByIds(userSet);

    assertTrue(result);
  }


  @Test
  void testIsAuthorsExistsInDbByIds_SomeAuthorsDoNotExist_ReturnsFalse() {
    Set<Long> userSet = TestConstants.UserConstants.SET_OF_USER_IDS;

    when(userRepository.existsById(userSet.stream().findFirst().orElse(null))).thenReturn(true);
    when(userRepository.existsById(userSet.stream().skip(1).findFirst().orElse(null))).thenReturn(false);

    boolean result = courseValidator.isAuthorsExistsInDbByIds(userSet);

    assertFalse(result);
  }


  @Test
  void testIsAuthorsExistsInDbByIds_EmptyAuthorIdsList_ReturnsTrue() {
    boolean result =
        courseValidator.isAuthorsExistsInDbByIds(
            TestConstants.UserConstants.EMPTY_SET_OF_USER_IDS);

    assertTrue(result);
  }

  @Test
  void testIsStudentEnrolledInCourseWithTask_StudentEnrolled_ReturnsTrue() {
    when(courseRepository.existsByTasksIdAndStudentsContains(
            TestConstants.TaskConstants.TASK_ID, TestConstants.UserConstants.USER_ID))
        .thenReturn(true);

    boolean result =
        courseValidator.isStudentEnrolledInCourseWithTask(
            TestConstants.TaskConstants.TASK_ID, TestConstants.UserConstants.USER_ID);

    assertTrue(result);
  }

  @Test
  void testIsStudentEnrolledInCourseWithTask_StudentNotEnrolled_ReturnsFalse() {
    when(courseRepository.existsByTasksIdAndStudentsContains(
            TestConstants.TaskConstants.TASK_ID, TestConstants.UserConstants.USER_ID))
        .thenReturn(false);

    boolean result =
        courseValidator.isStudentEnrolledInCourseWithTask(
            TestConstants.TaskConstants.TASK_ID, TestConstants.UserConstants.USER_ID);

    assertFalse(result);
  }
}
