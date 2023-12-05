package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.persistence.entity.Submission;
import com.illiapinchuk.moodle.persistence.repository.SubmissionRepository;
import com.illiapinchuk.moodle.service.SubmissionService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

  private final SubmissionRepository submissionRepository;

  @Override
  public Submission saveSubmission(Submission submission) {
    return submissionRepository.save(submission);
  }

  @Override
  public Submission getSubmissionById(@Nonnull final String id) {
    return submissionRepository.findById(id).orElseThrow();
  }

  @Override
  public List<Submission> getSubmissionsByTaskIdAndStudentId(@Nonnull final String taskId, @Nonnull final Long studentId) {
    return submissionRepository.getSubmissionsByTaskIdAndAndUserId(taskId, studentId);
  }
}
