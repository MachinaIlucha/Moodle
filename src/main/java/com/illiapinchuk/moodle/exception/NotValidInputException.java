package com.illiapinchuk.moodle.exception;

import java.io.Serial;

/**
 * Exception thrown when the input provided is not valid.
 */
public class NotValidInputException extends RuntimeException{
  @Serial
  private static final long serialVersionUID = 2859348592436194461L;

  public NotValidInputException(String message) {
    super(message);
  }
}
