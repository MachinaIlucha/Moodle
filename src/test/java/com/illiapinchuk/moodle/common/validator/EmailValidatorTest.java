package com.illiapinchuk.moodle.common.validator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {

  private static final List<String> VALID_EMAILS =
      List.of("test@example.com", "john.doe@gmail.com");

  private static final List<String> INVALID_EMAILS =
      List.of("invalidemail", "john.doe", "john.doe@com");

  @Test
  void testValidEmail() {
    EmailValidator emailValidator = new EmailValidator();

    for (String email : VALID_EMAILS) {
      assertTrue(emailValidator.isValid(email, null));
    }
  }

  @Test
  void testInvalidEmail() {
    EmailValidator emailValidator = new EmailValidator();

    for (String email : INVALID_EMAILS) {
      assertFalse(emailValidator.isValid(email, null));
    }
  }
}
