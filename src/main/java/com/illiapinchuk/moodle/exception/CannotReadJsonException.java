package com.illiapinchuk.moodle.exception;

import java.io.Serial;

/** Exception thrown when there is an error reading JSON data. */
public class CannotReadJsonException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1516628644067572936L;

  public CannotReadJsonException(String message) {
    super(message);
  }
}
