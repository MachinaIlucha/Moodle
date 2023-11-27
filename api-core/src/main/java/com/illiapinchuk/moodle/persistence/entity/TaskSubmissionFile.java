package com.illiapinchuk.moodle.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.illiapinchuk.moodle.common.ApplicationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/** TaskSubmissionFile class represents a Task submissions files. */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskSubmissionFile {

  @JsonIgnore
  @NotNull(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_TASKID_BLANK_ERROR_MESSAGE)
  @NotBlank(
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_TASKID_NULL_ERROR_MESSAGE)
  String taskId;

  @NotNull(
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_USERID_NULL_ERROR_MESSAGE)
  @NotBlank(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_USERID_BLANK_ERROR_MESSAGE)
  Long userId;

  @NotNull(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage
              .SUBMISSION_FILENAME_BLANK_ERROR_MESSAGE)
  @NotBlank(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage
              .SUBMISSION_FILENAME_NULL_ERROR_MESSAGE)
  String fileName;
}
