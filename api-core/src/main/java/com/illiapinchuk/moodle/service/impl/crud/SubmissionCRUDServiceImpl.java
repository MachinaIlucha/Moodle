package com.illiapinchuk.moodle.service.impl.crud;

import com.illiapinchuk.moodle.persistence.entity.Submission;
import com.illiapinchuk.moodle.persistence.repository.SubmissionRepository;
import com.illiapinchuk.moodle.service.crud.SubmissionCRUDService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Implementation of {@link SubmissionCRUDService} interface. */
@Service
@RequiredArgsConstructor
public class SubmissionCRUDServiceImpl implements SubmissionCRUDService {

  private final SubmissionRepository submissionRepository;

  @Override
  public Submission saveSubmission(Submission submission) {
    return submissionRepository.save(submission);
  }

  @Override
  public Submission getSubmissionById(@Nonnull final String id) {
    return submissionRepository.findById(id).orElseThrow();
  }
}
