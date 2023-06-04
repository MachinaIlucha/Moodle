package com.illiapinchuk.moodle.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * Represents an API error response.
 * <p>
 * This class encapsulates information about an API error response. It includes details such as the HTTP status,
 * timestamp, error message, debug message, and sub-errors (if applicable).
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ApiError {

  HttpStatus status;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  LocalDateTime timestamp;
  String message;
  String debugMessage;
  List<ApiSubError> subErrors;

  private ApiError() {
    timestamp = LocalDateTime.now();
  }

  public ApiError(HttpStatus status) {
    this();
    this.status = status;
  }

  public ApiError(HttpStatus status, Throwable ex) {
    this();
    this.status = status;
    this.message = "Unexpected error";
    this.debugMessage = ex.getLocalizedMessage();
  }

  public ApiError(HttpStatus status, String message, Throwable ex) {
    this();
    this.status = status;
    this.message = message;
    this.debugMessage = ex.getLocalizedMessage();
  }

  private void addSubError(ApiSubError subError) {
    if (subErrors == null) {
      subErrors = new ArrayList<>();
    }
    subErrors.add(subError);
  }

  /**
   * Utility methods for adding validation errors to the API error.
   * <p>
   * This class provides utility methods to add validation errors to the {@link ApiError} object. These methods are used
   * when handling validation errors in the API and adding corresponding sub-errors or error details to the API error response.
   */
  private void addValidationError(final String object, final String field,
                                  final Object rejectedValue,
                                  final String message) {
    addSubError(new ApiValidationError(object, field, rejectedValue, message));
  }

  /**
   * Utility method for adding a validation error with only the object name and message.
   *
   * @param object  the name of the object being validated
   * @param message the error message
   */
  private void addValidationError(final String object, final String message) {
    addSubError(new ApiValidationError(object, message));
  }

  /**
   * Utility method for adding a validation error based on a FieldError object.
   *
   * @param fieldError the FieldError object representing the validation error
   */
  private void addValidationError(@NotNull final FieldError fieldError) {
    this.addValidationError(
        fieldError.getObjectName(),
        fieldError.getField(),
        fieldError.getRejectedValue(),
        fieldError.getDefaultMessage());
  }

  /**
   * Utility method for adding multiple validation errors based on a list of FieldError objects.
   *
   * @param fieldErrors the list of FieldError objects representing the validation errors
   */
  public void addValidationErrors(@NotNull final List<FieldError> fieldErrors) {
    fieldErrors.forEach(this::addValidationError);
  }

  /**
   * Utility method for adding a validation error based on an ObjectError object.
   *
   * @param objectError the ObjectError object representing the validation error
   */
  private void addValidationError(@NotNull final ObjectError objectError) {
    this.addValidationError(
        objectError.getObjectName(),
        objectError.getDefaultMessage());
  }

  /**
   * Utility method for adding multiple validation errors based on a list of ObjectError objects.
   *
   * @param globalErrors the list of ObjectError objects representing the validation errors
   */
  public void addValidationError(@NotNull final List<ObjectError> globalErrors) {
    globalErrors.forEach(this::addValidationError);
  }

  /**
   * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
   *
   * @param cv the ConstraintViolation
   */
  private void addValidationError(@NotNull final ConstraintViolation<?> cv) {
    this.addValidationError(
        cv.getRootBeanClass().getSimpleName(),
        ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
        cv.getInvalidValue(),
        cv.getMessage());
  }


  /**
   * Utility method for adding multiple validation errors based on a set of ConstraintViolations.
   *
   * @param constraintViolations the set of ConstraintViolations representing the validation errors
   */
  public void addValidationErrors(@NotNull final Set<ConstraintViolation<?>> constraintViolations) {
    constraintViolations.forEach(this::addValidationError);
  }

}
