package com.illiapinchuk.moodle.common.validator;

import com.illiapinchuk.moodle.common.annotation.ValidEmail;
import com.illiapinchuk.moodle.common.ApplicationConstants;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

/**
 * Class validate an email address using a regular expression. The class implements the {@link
 * ConstraintValidator} interface.
 */
@Component
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

  @Override
  public boolean isValid(
      @NotNull final String email, @Nullable final ConstraintValidatorContext context) {
    return email != null && validateEmail(email);
  }

  private boolean validateEmail(@NotNull final String email) {
    final var matcher = ApplicationConstants.Validation.EMAIL_PATTERN.matcher(email);
    return matcher.matches();
  }
}
