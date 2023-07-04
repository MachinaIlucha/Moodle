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

@ExtendWith(MockitoExtension.class)
class CourseValidatorTest {

  @Mock private CourseRepository courseRepository;

  @Mock private UserRepository userRepository;

  @InjectMocks private CourseValidator courseValidator;

  @Test
  void testIsCourseExistsInDbById_ExistingCourseId_ReturnsTrue() {
    when(courseRepository.existsById(TestConstants.CourseConstants.VALID_COURSE_ID)).thenReturn(true);

    boolean result =
        courseValidator.isCourseExistsInDbById(TestConstants.CourseConstants.VALID_COURSE_ID);

    assertTrue(result);
  }

  @Test
  void testIsCourseExistsInDbById_NonExistingCourseId_ReturnsFalse() {
    when(courseRepository.existsById(TestConstants.CourseConstants.VALID_COURSE_ID)).thenReturn(false);

    boolean result =
        courseValidator.isCourseExistsInDbById(TestConstants.CourseConstants.VALID_COURSE_ID);

    assertFalse(result);
  }

  @Test
  void testIsAuthorsExistsInDbByIds_AllAuthorsExist_ReturnsTrue() {
    when(userRepository.existsById(
            Long.valueOf(TestConstants.UserConstants.LIST_OF_USER_IDS.get(0))))
        .thenReturn(true);
    when(userRepository.existsById(
            Long.valueOf(TestConstants.UserConstants.LIST_OF_USER_IDS.get(1))))
        .thenReturn(true);
    when(userRepository.existsById(
            Long.valueOf(TestConstants.UserConstants.LIST_OF_USER_IDS.get(2))))
        .thenReturn(true);

    boolean result =
        courseValidator.isAuthorsExistsInDbByIds(TestConstants.UserConstants.LIST_OF_USER_IDS);

    assertTrue(result);
  }

  @Test
  void testIsAuthorsExistsInDbByIds_SomeAuthorsDoNotExist_ReturnsFalse() {
    when(userRepository.existsById(
            Long.valueOf(TestConstants.UserConstants.LIST_OF_USER_IDS.get(0))))
        .thenReturn(true);
    when(userRepository.existsById(
            Long.valueOf(TestConstants.UserConstants.LIST_OF_USER_IDS.get(1))))
        .thenReturn(false);

    boolean result =
        courseValidator.isAuthorsExistsInDbByIds(TestConstants.UserConstants.LIST_OF_USER_IDS);

    assertFalse(result);
  }

  @Test
  void testIsAuthorsExistsInDbByIds_EmptyAuthorIdsList_ReturnsTrue() {
    boolean result =
        courseValidator.isAuthorsExistsInDbByIds(
            TestConstants.UserConstants.EMPTY_LIST_OF_USER_IDS);

    assertTrue(result);
  }
}
