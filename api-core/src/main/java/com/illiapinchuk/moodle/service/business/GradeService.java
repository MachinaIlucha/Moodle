package com.illiapinchuk.moodle.service.business;

import com.illiapinchuk.moodle.model.dto.GradeDto;

/** The GradeService interface provides methods for managing grades for submissions. */
public interface GradeService {

  /**
   * Grades a student's submission for a specific task.
   *
   * @param taskId The unique identifier of the task for which the submission is graded.
   * @param submissionId The unique identifier of the student's submission to be graded.
   * @param gradeDto A GradeDto object containing the grade information, including score, grade
   *     letter, comments, and graded timestamp.
   */
  void gradeSubmission(String taskId, String submissionId, GradeDto gradeDto);
}
