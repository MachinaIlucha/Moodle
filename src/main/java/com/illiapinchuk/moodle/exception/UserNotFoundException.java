package com.illiapinchuk.moodle.exception;

import jakarta.persistence.EntityNotFoundException;
import java.io.Serial;

/**
 * An exception class that indicates that a requested user was not found.
 * This class extends from the {@link EntityNotFoundException} class.
 */
public class UserNotFoundException extends EntityNotFoundException {

  @Serial
  private static final long serialVersionUID = 3433713677665693115L;

  public UserNotFoundException(String message) {
    super(message);
  }
}
