package com.illiapinchuk.moodle.persistence.entity;

import com.illiapinchuk.moodle.common.constants.ApplicationConstants;
import com.illiapinchuk.moodle.model.entity.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/** Task class represents a Task in the mongo db. */
@Document(collection = "tasks")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {

  @Id String id;

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

  @NotNull(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_COURSE_BLANK_ERROR_MESSAGE)
  @DBRef
  Course course;

  @NotBlank(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_AUTHOR_BLANK_ERROR_MESSAGE)
  String authorId;

  @NotNull(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_STATUS_BLANK_ERROR_MESSAGE)
  TaskStatus status;

  List<TaskAttachment> attachments = new ArrayList<>();

  List<Submission> submissions = new ArrayList<>();

}
