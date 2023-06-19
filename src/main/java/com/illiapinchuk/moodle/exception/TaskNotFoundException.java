package com.illiapinchuk.moodle.exception;

import jakarta.persistence.EntityNotFoundException;
import java.io.Serial;

/**
 * An exception class that indicates that a requested task was not found. This class extends from
 * the {@link EntityNotFoundException} class.
 */
public class TaskNotFoundException extends EntityNotFoundException {
  @Serial
  private static final long serialVersionUID = 2996373584732211343L;

  public TaskNotFoundException(String message) {
    super(message);
  }
}
