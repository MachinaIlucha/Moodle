package com.illiapinchuk.moodle.persistence.entity;

import com.illiapinchuk.moodle.model.entity.GradeStatus;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import static com.illiapinchuk.moodle.common.ApplicationConstants.Web.DataValidation.ErrorMessage.GRADE_COMMENTS_SIZE_ERROR_MESSAGE;
import static com.illiapinchuk.moodle.common.ApplicationConstants.Web.DataValidation.ErrorMessage.GRADE_COURSEID_BLANK_ERROR_MESSAGE;
import static com.illiapinchuk.moodle.common.ApplicationConstants.Web.DataValidation.ErrorMessage.GRADE_GRADED_AT_ERROR_MESSAGE;
import static com.illiapinchuk.moodle.common.ApplicationConstants.Web.DataValidation.ErrorMessage.GRADE_SCORE_ERROR_MESSAGE;
import static com.illiapinchuk.moodle.common.ApplicationConstants.Web.DataValidation.ErrorMessage.GRADE_STUDENTID_NULL_ERROR_MESSAGE;
import static com.illiapinchuk.moodle.common.ApplicationConstants.Web.DataValidation.ErrorMessage.GRADE_TASKID_BLANK_ERROR_MESSAGE;
import static com.illiapinchuk.moodle.common.ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_GRADE_COMMENTS;

@Document(collection = "grades")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Grade {

  @Id String id;

  @NotNull(message = GRADE_STUDENTID_NULL_ERROR_MESSAGE)
  Long studentId;

  @NotBlank(message = GRADE_TASKID_BLANK_ERROR_MESSAGE)
  String taskId;

  @NotBlank(message = GRADE_COURSEID_BLANK_ERROR_MESSAGE)
  String courseId;

  @Min(value = 0, message = GRADE_SCORE_ERROR_MESSAGE)
  int score;

  @Size(max = MAX_SIZE_OF_GRADE_COMMENTS, message = GRADE_COMMENTS_SIZE_ERROR_MESSAGE)
  String comments;

  @PastOrPresent(message = GRADE_GRADED_AT_ERROR_MESSAGE)
  LocalDateTime gradedAt;

  @Builder.Default GradeStatus gradeStatus = GradeStatus.NOT_RATED;
}
