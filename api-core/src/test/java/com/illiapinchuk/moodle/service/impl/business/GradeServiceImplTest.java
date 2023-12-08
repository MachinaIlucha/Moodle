package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.exception.NotValidInputException;
import com.illiapinchuk.moodle.exception.SubmissionAlreadyGradedException;
import com.illiapinchuk.moodle.model.dto.GradeDto;
import com.illiapinchuk.moodle.model.entity.GradeStatus;
import com.illiapinchuk.moodle.persistence.entity.Grade;
import com.illiapinchuk.moodle.persistence.entity.Submission;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.persistence.repository.GradeRepository;
import com.illiapinchuk.moodle.service.crud.SubmissionCRUDService;
import com.illiapinchuk.moodle.service.crud.TaskCRUDService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GradeServiceImplTest {

  @Mock private SubmissionCRUDService submissionCRUDService;
  @Mock private TaskCRUDService taskCRUDService;
  @Mock private GradeRepository gradeRepository;

  @InjectMocks private GradeServiceImpl gradeService;

  @Test
  void gradeSubmission_SubmissionAlreadyGraded_ThrowsException() {
    final var taskId = TestConstants.TaskConstants.TASK_ID;
    final var submissionId = "submission1";
    final var gradeDto = new GradeDto();
    final var task = new Task();
    final var submission = new Submission();
    final var grade = new Grade();
    grade.setGradeStatus(GradeStatus.RATED);
    submission.setGrade(grade);

    when(taskCRUDService.getTaskById(taskId)).thenReturn(task);
    when(submissionCRUDService.getSubmissionById(submissionId)).thenReturn(submission);

    assertThrows(
        SubmissionAlreadyGradedException.class,
        () -> gradeService.gradeSubmission(taskId, submissionId, gradeDto));
  }

  @Test
  void gradeSubmission_ScoreGreaterThanMaxScore_ThrowsException() {
    final var taskId = TestConstants.TaskConstants.TASK_ID;
    final var submissionId = "submission1";
    final var gradeDto = new GradeDto();
    gradeDto.setScore(100);
    final var task = new Task();
    task.setMaxScore(50);
    final var submission = new Submission();
    final var grade = new Grade();
    grade.setGradeStatus(GradeStatus.NOT_RATED);
    submission.setGrade(grade);

    when(taskCRUDService.getTaskById(taskId)).thenReturn(task);
    when(submissionCRUDService.getSubmissionById(submissionId)).thenReturn(submission);

    assertThrows(
        NotValidInputException.class,
        () -> gradeService.gradeSubmission(taskId, submissionId, gradeDto));
  }

  @Test
  void gradeSubmission_ValidSubmissionAndScore_GradesSubmission() {
    final var taskId = TestConstants.TaskConstants.TASK_ID;
    final var submissionId = "submission1";
    final var gradeDto = new GradeDto();
    gradeDto.setScore(50);
    final var task = new Task();
    task.setMaxScore(100);
    final var submission = new Submission();
    final var grade = new Grade();
    grade.setGradeStatus(GradeStatus.NOT_RATED);
    submission.setGrade(grade);

    when(taskCRUDService.getTaskById(taskId)).thenReturn(task);
    when(submissionCRUDService.getSubmissionById(submissionId)).thenReturn(submission);

    assertDoesNotThrow(() -> gradeService.gradeSubmission(taskId, submissionId, gradeDto));
    assertEquals(GradeStatus.RATED, grade.getGradeStatus());
    assertEquals(50, grade.getScore());
  }

  @Test
  void gradeSubmission_NullGrade_ThrowsNullPointerException() {
    final var taskId = TestConstants.TaskConstants.TASK_ID;
    final var submissionId = "submission1";
    final var gradeDto = new GradeDto();
    final var task = new Task();
    final var submission = new Submission();

    when(taskCRUDService.getTaskById(taskId)).thenReturn(task);
    when(submissionCRUDService.getSubmissionById(submissionId)).thenReturn(submission);

    assertThrows(
        NullPointerException.class,
        () -> gradeService.gradeSubmission(taskId, submissionId, gradeDto));
  }
}
