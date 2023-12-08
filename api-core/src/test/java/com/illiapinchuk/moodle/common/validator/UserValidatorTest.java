package com.illiapinchuk.moodle.common.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

  @Mock private UserRepository userRepository;

  @Mock private CourseValidator courseValidator;

  @InjectMocks private UserValidator userValidator;

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
  void checkIfUserHasAccessToCourse_UserHasAccess_DoesNotThrowException() {
    final var courseId = TestConstants.CourseConstants.VALID_COURSE_ID;
    final var userId = TestConstants.UserConstants.USER_ID;

    when(courseValidator.isStudentEnrolledInCourse(courseId, userId)).thenReturn(true);

    assertDoesNotThrow(() -> userValidator.checkIfUserHasAccessToCourse(courseId));
  }

  @Test
  void checkIfUserHasAccessToCourse_UserDoesNotHaveAccess_ThrowsException() {
    final var courseId = TestConstants.CourseConstants.VALID_COURSE_ID;
    final var userId = TestConstants.UserConstants.USER_ID;

    when(courseValidator.isStudentEnrolledInCourse(courseId, userId)).thenReturn(false);
    when(UserPermissionService.hasAnyRulingRole()).thenReturn(false);

    assertThrows(UserDontHaveAccessToResource.class, () -> userValidator.checkIfUserHasAccessToCourse(courseId));
  }

  @Test
  void testIsLoginExistInDb_ValidLogin_ReturnsTrue() {
    when(userRepository.existsUserByLogin(TestConstants.UserConstants.USER_LOGIN)).thenReturn(true);

    boolean result = userValidator.isLoginExistInDb(TestConstants.UserConstants.USER_LOGIN);

    assertTrue(result);
    verify(userRepository).existsUserByLogin(TestConstants.UserConstants.USER_LOGIN);
  }

  @Test
  void testIsLoginExistInDb_InvalidLogin_ReturnsFalse() {
    when(userRepository.existsUserByLogin(TestConstants.UserConstants.USER_LOGIN))
        .thenReturn(false);

    boolean result = userValidator.isLoginExistInDb(TestConstants.UserConstants.USER_LOGIN);

    assertFalse(result);
    verify(userRepository).existsUserByLogin(TestConstants.UserConstants.USER_LOGIN);
  }

  @Test
  void testIsEmailExistInDb_ValidEmail_ReturnsTrue() {
    when(userRepository.existsUserByEmail(TestConstants.UserConstants.USER_EMAIL)).thenReturn(true);

    boolean result = userValidator.isEmailExistInDb(TestConstants.UserConstants.USER_EMAIL);

    assertTrue(result);
    verify(userRepository).existsUserByEmail(TestConstants.UserConstants.USER_EMAIL);
  }

  @Test
  void testIsEmailExistInDb_InvalidEmail_ReturnsFalse() {
    when(userRepository.existsUserByEmail(TestConstants.UserConstants.USER_EMAIL))
        .thenReturn(false);

    boolean result = userValidator.isEmailExistInDb(TestConstants.UserConstants.USER_EMAIL);

    assertFalse(result);
    verify(userRepository).existsUserByEmail(TestConstants.UserConstants.USER_EMAIL);
  }

  @Test
  void testIsUserExistInDbById_ValidId_ReturnsTrue() {
    when(userRepository.existsById(TestConstants.UserConstants.USER_ID)).thenReturn(true);

    boolean result = userValidator.isUserExistInDbById(TestConstants.UserConstants.USER_ID);

    assertTrue(result);
    verify(userRepository).existsById(TestConstants.UserConstants.USER_ID);
  }

  @Test
  void testIsUserExistInDbById_InvalidId_ReturnsFalse() {
    when(userRepository.existsById(TestConstants.UserConstants.USER_ID)).thenReturn(false);

    boolean result = userValidator.isUserExistInDbById(TestConstants.UserConstants.USER_ID);

    assertFalse(result);
    verify(userRepository).existsById(TestConstants.UserConstants.USER_ID);
  }
}
