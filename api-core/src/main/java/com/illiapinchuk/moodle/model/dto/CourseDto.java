package com.illiapinchuk.moodle.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.illiapinchuk.moodle.common.ApplicationConstants;
import com.illiapinchuk.moodle.persistence.entity.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/** Incoming DTO to represent {@link Course}. */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  String id;

  @NotBlank(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.COURSE_NAME_BLANK_ERROR_MESSAGE)
  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_COURSE_NAME,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_COURSE_NAME,
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.COURSE_NAME_SIZE_ERROR_MESSAGE)
  String name;

  @NotBlank(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage
              .COURSE_DESCRIPTION_BLANK_ERROR_MESSAGE)
  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_COURSE_DESCRIPTION,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_COURSE_DESCRIPTION,
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage
              .COURSE_DESCRIPTION_SIZE_ERROR_MESSAGE)
  String description;

  @NotEmpty(
          message =
                  ApplicationConstants.Web.DataValidation.ErrorMessage.COURSE_AUTHORS_EMPTY_ERROR_MESSAGE)
  Set<@NotNull Long> authorIds;

  Set<@NotNull Long> students;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  List<@NotBlank String> tasks;
}
