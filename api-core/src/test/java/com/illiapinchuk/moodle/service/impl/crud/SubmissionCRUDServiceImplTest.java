package com.illiapinchuk.moodle.service.impl.crud;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.persistence.entity.Submission;
import com.illiapinchuk.moodle.persistence.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.TASK_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmissionCRUDServiceImplTest {

  @Mock private SubmissionRepository submissionRepository;

  @InjectMocks private SubmissionCRUDServiceImpl submissionService;

  @Test
  void getSubmissionById_SubmissionExists_ReturnsSubmission() {
    final var submissionId = TASK_ID;
    final var expectedSubmission = new Submission();

    when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(expectedSubmission));

    final var actualSubmission = submissionService.getSubmissionById(submissionId);

    assertEquals(expectedSubmission, actualSubmission);
  }

  @Test
  void getSubmissionById_SubmissionDoesNotExist_ThrowsException() {
    final var submissionId = "invalidSubmissionId";

    when(submissionRepository.findById(submissionId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> submissionService.getSubmissionById(submissionId));
  }

  @Test
  void saveSubmission_ValidSubmission_SavesAndReturnsSubmission() {
    final var submission = new Submission();

    when(submissionRepository.save(submission)).thenReturn(submission);

    final var savedSubmission = submissionService.saveSubmission(submission);

    assertEquals(submission, savedSubmission);
  }
}
