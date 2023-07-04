package com.illiapinchuk.moodle.common.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.illiapinchuk.moodle.common.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailValidatorTest {

  private final EmailValidator emailValidator = new EmailValidator();

  @Test
  void testValidEmail() {
    assertTrue(emailValidator.isValid(TestConstants.UserConstants.USER_EMAIL, null));
  }

  @Test
  void testInvalidEmail() {
    assertFalse(emailValidator.isValid(TestConstants.UserConstants.USER_INVALID_EMAIL, null));
  }

  @Test
  void testNullEmail() {
    assertFalse(emailValidator.isValid(TestConstants.UserConstants.USER_NULL_EMAIL, null));
  }

  @Test
  void testEmptyEmail() {
    assertFalse(emailValidator.isValid(TestConstants.UserConstants.USER_EMPTY_EMAIL, null));
  }

  @Test
  void testEmailWithLeadingWhitespace() {
    assertFalse(
        emailValidator.isValid(TestConstants.UserConstants.USER_EMAIL_WITH_WHITESPACE, null));
  }

  @Test
  void testEmailWithTrailingWhitespace() {
    assertFalse(
        emailValidator.isValid(TestConstants.UserConstants.USER_EMAIL_WITH_WHITESPACE, null));
  }

  @Test
  void testEmailWithWhitespaceInside() {
    assertFalse(
        emailValidator.isValid(TestConstants.UserConstants.USER_EMAIL_WITH_WHITESPACE, null));
  }
}
