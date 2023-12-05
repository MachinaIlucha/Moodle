package com.illiapinchuk.moodle.common.validator;

import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** Validation for user-related information. */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidator {

  private final UserRepository userRepository;

  private final CourseValidator courseValidator;

  /**
   * Check if the given login exists in the database.
   *
   * @param login the login to check
   * @return true if the login exists in the database, false otherwise
   */
  public boolean isLoginExistInDb(@Nonnull final String login) {
    return userRepository.existsUserByLogin(login);
  }

  /**
   * Check if the given email exists in the database.
   *
   * @param email the email to check
   * @return true if the email exists in the database, false otherwise
   */
  public boolean isEmailExistInDb(@Nonnull final String email) {
    return userRepository.existsUserByEmail(email);
  }

  /**
   * Check if the given user with id exists in the database.
   *
   * @param id the id to check
   * @return true if the user exists in the database, false otherwise
   */
  public boolean isUserExistInDbById(@Nonnull final Long id) {
    return userRepository.existsById(id);
  }

  /**
   * Checks if the user identified by their JWT token has access to a specific course.
   *
   * @param courseId The ID of the course to check access for.
   * @throws UserDontHaveAccessToResource If the user doesn't have access to the specified course.
   */
  public void checkIfUserHasAccessToCourse(@Nonnull final String courseId) {
    final var userId = UserPermissionService.getJwtUser().getId();

    if (!courseValidator.isStudentEnrolledInCourse(courseId, userId)
        && !UserPermissionService.hasAnyRulingRole()) {
      log.error("User with id - {} trying to access course with id - {}", userId, courseId);
      throw new UserDontHaveAccessToResource("User doesn't have access to this course.");
    }
  }
}
