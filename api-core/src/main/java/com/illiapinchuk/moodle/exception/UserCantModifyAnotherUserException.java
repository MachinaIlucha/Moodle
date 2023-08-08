package com.illiapinchuk.moodle.exception;

import java.io.Serial;

/**
 * Represents an exception thrown when a user tries to modify another user's data without
 * appropriate permissions or authority.
 */
public class UserCantModifyAnotherUserException extends RuntimeException {

  @Serial private static final long serialVersionUID = 848748022850902704L;

  public UserCantModifyAnotherUserException(String message) {
    super(message);
  }
}
