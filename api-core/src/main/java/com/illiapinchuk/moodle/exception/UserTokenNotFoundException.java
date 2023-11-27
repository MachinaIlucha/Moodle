package com.illiapinchuk.moodle.exception;

import jakarta.persistence.EntityNotFoundException;

import java.io.Serial;

/**
 * Exception class for situations where a user token is not found. This class extends {@link
 * EntityNotFoundException} from jakarta.persistence package, indicating that this exception should
 * be thrown when a specific user token expected to be present in the database is not found.
 *
 * <p>This exception is part of the exception hierarchy in the moodle package, specifically designed
 * to handle errors related to user authentication and token management.
 */
public class UserTokenNotFoundException extends EntityNotFoundException {
  @Serial private static final long serialVersionUID = -5409211867265639337L;

  public UserTokenNotFoundException(String message) {
    super(message);
  }
}
