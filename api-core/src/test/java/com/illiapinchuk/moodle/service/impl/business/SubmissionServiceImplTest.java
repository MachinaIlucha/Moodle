package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.persistence.entity.Submission;
import com.illiapinchuk.moodle.persistence.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.TASK_ID;
import static com.illiapinchuk.moodle.common.TestConstants.UserConstants.USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceImplTest {

  @Mock private SubmissionRepository submissionRepository;

  @InjectMocks private SubmissionServiceImpl submissionService;

  @Test
  void getSubmissionsByTaskIdAndStudentIdTest() {
    final var taskId = TASK_ID;
    final var studentId = USER_ID;
    final var submission1 = new Submission();
    final var submission2 = new Submission();
    final List<Submission> expectedSubmissions = Arrays.asList(submission1, submission2);

    when(submissionRepository.getSubmissionsByTaskIdAndAndUserId(taskId, studentId))
        .thenReturn(expectedSubmissions);

    final var actualSubmissions =
        submissionService.getSubmissionsByTaskIdAndStudentId(taskId, studentId);

    assertEquals(expectedSubmissions, actualSubmissions);
  }
}
