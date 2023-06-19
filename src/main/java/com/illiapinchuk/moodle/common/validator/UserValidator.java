package com.illiapinchuk.moodle.common.validator;

import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Validation for user-related information. */
@Component
@RequiredArgsConstructor
public class UserValidator {

  private final UserRepository userRepository;

  /**
   * Check if the given login exists in the database.
   *
   * @param login the login to check
   * @return true if the login exists in the database, false otherwise
   */
  public boolean isLoginExistInDb(@NotNull final String login) {
    return userRepository.existsUserByLogin(login);
  }

  /**
   * Check if the given email exists in the database.
   *
   * @param email the email to check
   * @return true if the email exists in the database, false otherwise
   */
  public boolean isEmailExistInDb(@NotNull final String email) {
    return userRepository.existsUserByEmail(email);
  }
}
