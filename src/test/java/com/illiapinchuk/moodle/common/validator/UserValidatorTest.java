package com.illiapinchuk.moodle.common.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserValidator userValidator;

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
    when(userRepository.existsById(Long.valueOf(TestConstants.UserConstants.USER_ID)))
        .thenReturn(true);

    boolean result = userValidator.isUserExistInDbById(TestConstants.UserConstants.USER_ID);

    assertTrue(result);
    verify(userRepository).existsById(Long.valueOf(TestConstants.UserConstants.USER_ID));
  }

  @Test
  void testIsUserExistInDbById_InvalidId_ReturnsFalse() {
    when(userRepository.existsById(Long.valueOf(TestConstants.UserConstants.USER_ID)))
        .thenReturn(false);

    boolean result = userValidator.isUserExistInDbById(TestConstants.UserConstants.USER_ID);

    assertFalse(result);
    verify(userRepository).existsById(Long.valueOf(TestConstants.UserConstants.USER_ID));
  }
}
