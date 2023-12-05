package com.illiapinchuk.moodle.service.business;

import com.illiapinchuk.moodle.persistence.entity.Submission;

import java.util.List;

/** Service interface for managing submissions. */
public interface SubmissionService {

  /**
   * Retrieves a list of Submission objects based on the provided task ID and student ID.
   *
   * @param taskId The unique identifier of the task for which submissions are to be retrieved.
   * @param studentId The unique identifier of the student for whom submissions are to be retrieved.
   * @return A list of Submission objects that match the specified task ID and student ID, or an
   *     empty list if no submissions are found.
   */
  List<Submission> getSubmissionsByTaskIdAndStudentId(String taskId, Long studentId);
}
