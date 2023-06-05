package com.illiapinchuk.moodle.common.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

  private static final String EXISTING_LOGIN = "existing_login";
  private static final String NON_EXISTING_LOGIN = "non_existing_login";
  private static final String EXISTING_EMAIL = "existing_email@example.com";
  private static final String NON_EXISTING_EMAIL = "non_existing_email@example.com";

  @Mock
  private UserRepository userRepository;
  @InjectMocks
  private UserValidator userValidator;

  @Test
  void testIsLoginExistInDb_WhenLoginExists_ShouldReturnTrue() {
    when(userRepository.existsUserByLogin(EXISTING_LOGIN)).thenReturn(true);

    boolean result = userValidator.isLoginExistInDb(EXISTING_LOGIN);

    assertTrue(result);
  }

  @Test
  void testIsLoginExistInDb_WhenLoginDoesNotExist_ShouldReturnFalse() {
    when(userRepository.existsUserByLogin(NON_EXISTING_LOGIN)).thenReturn(false);

    boolean result = userValidator.isLoginExistInDb(NON_EXISTING_LOGIN);

    assertFalse(result);
  }

  @Test
  void testIsEmailExistInDb_WhenEmailExists_ShouldReturnTrue() {
    when(userRepository.existsUserByEmail(EXISTING_EMAIL)).thenReturn(true);

    boolean result = userValidator.isEmailExistInDb(EXISTING_EMAIL);

    assertTrue(result);
  }

  @Test
  void testIsEmailExistInDb_WhenEmailDoesNotExist_ShouldReturnFalse() {
    when(userRepository.existsUserByEmail(NON_EXISTING_EMAIL)).thenReturn(false);

    boolean result = userValidator.isEmailExistInDb(NON_EXISTING_EMAIL);

    assertFalse(result);
  }
}
