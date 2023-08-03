package com.illiapinchuk.moodle.exception;

import java.io.Serial;

/** Exception thrown when the jwt token is not valid. */
public class InvalidJwtTokenException extends RuntimeException {

  @Serial private static final long serialVersionUID = -3708586544102153251L;

  public InvalidJwtTokenException(String message) {
    super(message);
  }
}
