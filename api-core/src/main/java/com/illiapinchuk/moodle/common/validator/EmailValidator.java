package com.illiapinchuk.moodle.common.validator;

import com.illiapinchuk.moodle.common.ApplicationConstants;
import com.illiapinchuk.moodle.common.annotation.ValidEmail;
import jakarta.annotation.Nonnull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

/**
 * Class validate an email address using a regular expression. The class implements the {@link
 * ConstraintValidator} interface.
 */
@Component
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

  @Override
  public boolean isValid(
      @Nonnull final String email, @Nonnull final ConstraintValidatorContext context) {
    return validateEmail(email);
  }

  private boolean validateEmail(@Nonnull final String email) {
    final var matcher = ApplicationConstants.Validation.EMAIL_PATTERN.matcher(email);
    return matcher.matches();
  }
}
