package com.illiapinchuk.moodle.persistence.entity;

import com.illiapinchuk.moodle.common.ApplicationConstants;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/** Submission class represents a submission in the task. */
@Document(collection = "submissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Submission {

  @Id String id;

  @NotNull(
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_USERID_NULL_ERROR_MESSAGE)
  @NotBlank(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_USERID_BLANK_ERROR_MESSAGE)
  Long userId;

  @NotNull(
      message =
          ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_TASKID_BLANK_ERROR_MESSAGE)
  String taskId;

  @Size(
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_TASK_ANSWER,
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.TASK_ANSWER_SIZE_ERROR_MESSAGE)
  String answer;

  @DBRef
  Grade grade;

  List<TaskSubmissionFile> submissionFiles = new ArrayList<>();
}
