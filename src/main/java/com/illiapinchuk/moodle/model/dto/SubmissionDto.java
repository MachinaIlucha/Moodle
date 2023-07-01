package com.illiapinchuk.moodle.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.illiapinchuk.moodle.common.constants.ApplicationConstants;
import com.illiapinchuk.moodle.persistence.entity.Submission;
import com.illiapinchuk.moodle.persistence.entity.TaskSubmissionFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/** Incoming DTO to represent {@link Submission}. */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDto {

  @NotNull(
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_USERID_NULL_ERROR_MESSAGE)
  @NotBlank(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_USERID_BLANK_ERROR_MESSAGE)
  String userId;

  @NotNull(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_TASKID_BLANK_ERROR_MESSAGE)
  @NotBlank(
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_TASKID_NULL_ERROR_MESSAGE)
  String taskId;

  @Size(
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_TASK_ANSWER,
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_ANSWER_SIZE_ERROR_MESSAGE)
  String answer;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  List<TaskSubmissionFile> submissionFiles;

  @Size(
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_TASK_GRADE,
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_GRADE_SIZE_ERROR_MESSAGE)
  int grade;
}
