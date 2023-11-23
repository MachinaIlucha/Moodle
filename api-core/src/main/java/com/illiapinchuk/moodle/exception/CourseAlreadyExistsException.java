package com.illiapinchuk.moodle.exception;

import jakarta.persistence.EntityExistsException;

import java.io.Serial;

/**
 * An exception class that indicates that a requested course already exists. This class extends from
 * the {@link EntityExistsException} class.
 */
public class CourseAlreadyExistsException extends EntityExistsException {

  @Serial private static final long serialVersionUID = -3142544438255058118L;

  public CourseAlreadyExistsException(String message) {
    super(message);
  }
}
