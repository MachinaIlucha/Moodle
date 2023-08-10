package com.illiapinchuk.moodle.exception;

import java.io.Serial;

/**
 * Represents an exception thrown when a user doesn't have access to a specific resource.
 *
 * <p>This exception can be used to signal unauthorized access or when the user attempts to access a
 * resource without proper permissions or enrollment.
 */
public class UserDontHaveAccessToResource extends RuntimeException {
  @Serial private static final long serialVersionUID = -1247359889036013261L;

  public UserDontHaveAccessToResource(String message) {
    super(message);
  }
}
