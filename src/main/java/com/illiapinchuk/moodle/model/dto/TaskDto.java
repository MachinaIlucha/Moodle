package com.illiapinchuk.moodle.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.illiapinchuk.moodle.common.constants.ApplicationConstants;
import com.illiapinchuk.moodle.model.entity.TaskStatus;
import com.illiapinchuk.moodle.persistence.entity.Task;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.util.Date;
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
  private String id;

  @NotBlank(message = "Title is mandatory")
  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_TASK_TITLE,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_TASK_TITLE,
      message = "Title should have between 1 and 200 characters")
  private String title;

  @NotBlank(message = "Description is mandatory")
  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_TASK_DESCRIPTION,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_TASK_DESCRIPTION,
      message = "Description should have between 1 and 255 characters")
  private String description;

  @FutureOrPresent(message = "Due Date must be in the present or future")
  private Date dueDate;

  @PastOrPresent(message = "Creation Date must be in the past or present")
  private Date creationDate;

  @NotBlank(message = "Course ID is mandatory")
  private String courseId;

  @NotBlank(message = "Author ID is mandatory")
  private String authorId;

  @NotNull(message = "Status is mandatory")
  private TaskStatus status;
}
