package com.illiapinchuk.moodle.persistence.entity;

import com.illiapinchuk.moodle.common.constants.ApplicationConstants;
import com.illiapinchuk.moodle.model.entity.TaskStatus;
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

  @NotBlank(message = "Title is mandatory")
  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_TASK_TITLE,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_TASK_TITLE,
      message = "Title should have between 1 and 255 characters")
  String title;

  @NotBlank(message = "Description is mandatory")
  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_TASK_DESCRIPTION,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_TASK_DESCRIPTION,
      message = "Description should have between 1 and 255 characters")
  String description;

  @FutureOrPresent(message = "Due Date must be in the present or future")
  Date dueDate;

  @PastOrPresent(message = "Creation Date must be in the past or present")
  Date creationDate;

  @NotNull(message = "Course is mandatory")
  @DBRef
  Course course;

  @NotBlank(message = "Author ID is mandatory")
  String authorId;

  @NotNull(message = "Status is mandatory")
  TaskStatus status;

  // TODO create attachments
  //  private List<String> attachments;

  // TODO create students submissions
  //  private List<Submission> submissions;

}
