package com.illiapinchuk.moodle.exception;

import java.io.Serial;

/** Exception thrown when unable to write to Amazon S3. */
public class CannotWriteToS3Exception extends RuntimeException {

  @Serial private static final long serialVersionUID = 8854520771456854262L;

  public CannotWriteToS3Exception(String message) {
    super(message);
  }
}
