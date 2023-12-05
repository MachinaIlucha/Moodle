package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.persistence.entity.Submission;
import com.illiapinchuk.moodle.persistence.repository.SubmissionRepository;
import com.illiapinchuk.moodle.service.business.SubmissionService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/** Implementation of {@link SubmissionService} interface. */
@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

  private final SubmissionRepository submissionRepository;

  @Override
  public List<Submission> getSubmissionsByTaskIdAndStudentId(@Nonnull final String taskId, @Nonnull final Long studentId) {
    return submissionRepository.getSubmissionsByTaskIdAndAndUserId(taskId, studentId);
  }
}
