package com.illiapinchuk.moodle.exception;

import java.io.Serial;

/** Exception thrown when the jwt token was expired. */
public class JwtTokenExpiredException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1702478726152104557L;

  public JwtTokenExpiredException(String message) {
    super(message);
  }
}
