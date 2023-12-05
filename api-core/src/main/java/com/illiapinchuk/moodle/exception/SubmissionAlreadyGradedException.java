package com.illiapinchuk.moodle.exception;

import java.io.Serial;

/**
 * Exception class to represent the situation where a submission has already been graded. This
 * exception is typically thrown when an attempt is made to grade a submission that has already
 * received a grade.
 */
public class SubmissionAlreadyGradedException extends RuntimeException {
  @Serial private static final long serialVersionUID = -6108766455239395964L;

  public SubmissionAlreadyGradedException(String message) {
    super(message);
  }
}
