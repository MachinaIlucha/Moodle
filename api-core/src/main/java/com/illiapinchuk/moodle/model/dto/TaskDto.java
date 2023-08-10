package com.illiapinchuk.moodle.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.illiapinchuk.moodle.common.ApplicationConstants;
import com.illiapinchuk.moodle.model.entity.TaskStatus;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/** Incoming DTO to represent {@link Task}. */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  String id;

  @NotBlank(
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_TITLE_BLANK_ERROR_MESSAGE)
  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_TASK_TITLE,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_TASK_TITLE,
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_TITLE_SIZE_ERROR_MESSAGE)
  String title;

  @NotBlank(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_DESCRIPTION_BLANK_ERROR_MESSAGE)
  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_TASK_DESCRIPTION,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_TASK_DESCRIPTION,
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_DESCRIPTION_SIZE_ERROR_MESSAGE)
  String description;

  @FutureOrPresent(
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_DUEDATE_ERROR_MESSAGE)
  Date dueDate;

  @PastOrPresent(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_CREATIONDATE_ERROR_MESSAGE)
  Date creationDate;

  @NotBlank(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_COURSE_BLANK_ERROR_MESSAGE)
  String courseId;

  @NotNull(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_AUTHOR_BLANK_ERROR_MESSAGE)
  Long authorId;

  @NotNull(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_STATUS_BLANK_ERROR_MESSAGE)
  TaskStatus status;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  List<TaskAttachment> attachments;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  List<SubmissionDto> submissions;
}
