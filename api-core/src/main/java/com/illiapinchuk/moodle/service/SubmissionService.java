package com.illiapinchuk.moodle.service;

import com.illiapinchuk.moodle.persistence.entity.Submission;

import java.util.List;

public interface SubmissionService {

  Submission saveSubmission(Submission submission);

  Submission getSubmissionById(String id);

  List<Submission> getSubmissionsByTaskIdAndStudentId(String taskId, Long studentId);
}
