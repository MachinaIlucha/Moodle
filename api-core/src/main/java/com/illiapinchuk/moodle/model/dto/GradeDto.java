package com.illiapinchuk.moodle.model.dto;

import com.illiapinchuk.moodle.model.entity.GradeStatus;
import com.illiapinchuk.moodle.persistence.entity.Grade;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/** Incoming DTO to represent {@link Grade}. */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class GradeDto {

  /** The numeric score associated with the grade. */
  int score;

  /** Additional comments or remarks about the grade. */
  String comments;

  /** The date and time when the grade was assigned. */
  LocalDateTime gradedAt;

  /**
   * The status of the grade, indicating whether it has been rated or not. Default value is {@link
   * GradeStatus#NOT_RATED}.
   */
  @Builder.Default GradeStatus gradeStatus = GradeStatus.NOT_RATED;
}
