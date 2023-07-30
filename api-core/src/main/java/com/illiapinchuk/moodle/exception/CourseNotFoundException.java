package com.illiapinchuk.moodle.exception;

import jakarta.persistence.EntityNotFoundException;
import java.io.Serial;

/**
 * An exception class that indicates that a requested course was not found. This class extends from
 * the {@link EntityNotFoundException} class.
 */
public class CourseNotFoundException extends EntityNotFoundException {
  @Serial private static final long serialVersionUID = 8591153979876227200L;

  public CourseNotFoundException(String message) {
    super(message);
  }
}
