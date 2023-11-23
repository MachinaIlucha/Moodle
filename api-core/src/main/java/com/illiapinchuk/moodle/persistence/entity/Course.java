package com.illiapinchuk.moodle.persistence.entity;

import com.illiapinchuk.moodle.common.ApplicationConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/** Course class represents a Course in the mongo db. */
@Document(collection = "courses")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Course {

  @Id String id;

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

  @DBRef(lazy = true)
  List<@Valid Task> tasks = new ArrayList<>();
}
