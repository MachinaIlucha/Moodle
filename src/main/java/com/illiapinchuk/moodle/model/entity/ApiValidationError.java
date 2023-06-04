package com.illiapinchuk.moodle.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

/**
 * Represents a validation error in the API.
 * <p>
 * This class extends {@link ApiSubError} and provides specific fields to represent a validation error in the API.
 * It includes information such as the object name, field name, rejected value, and error message.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
class ApiValidationError extends ApiSubError {
  String object;
  String field;
  Object rejectedValue;
  String message;

  ApiValidationError(String object, String message) {
    this.object = object;
    this.message = message;
  }
}
