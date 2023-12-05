package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.exception.NotValidInputException;
import com.illiapinchuk.moodle.exception.SubmissionAlreadyGradedException;
import com.illiapinchuk.moodle.model.dto.GradeDto;
import com.illiapinchuk.moodle.model.entity.GradeStatus;
import com.illiapinchuk.moodle.persistence.repository.GradeRepository;
import com.illiapinchuk.moodle.service.business.GradeService;
import com.illiapinchuk.moodle.service.crud.SubmissionCRUDService;
import com.illiapinchuk.moodle.service.crud.TaskCRUDService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/** Implementation of {@link GradeService} interface. */
@Service
@RequiredArgsConstructor
@Slf4j
public class GradeServiceImpl implements GradeService {

  private final SubmissionCRUDService submissionCRUDService;
  private final TaskCRUDService taskCRUDService;

  private final GradeRepository gradeRepository;

  @Override
  public void gradeSubmission(
      @Nonnull final String taskId,
      @Nonnull final String submissionId,
      @Nonnull final GradeDto gradeDto) {
    final var task = taskCRUDService.getTaskById(taskId);
    final var submission = submissionCRUDService.getSubmissionById(submissionId);

    final var grade = submission.getGrade();

    if (grade.getGradeStatus().equals(GradeStatus.RATED)) {
      log.error("Submission {} is already graded", submissionId);
      throw new SubmissionAlreadyGradedException(
          String.format("Submission %s is already graded", submissionId));
    }

    if (gradeDto.getScore() > task.getMaxScore()) {
      log.error("Score {} is greater than max score {}", gradeDto.getScore(), task.getMaxScore());
      throw new NotValidInputException(
          String.format(
              "Score %s is greater than max score %s", gradeDto.getScore(), task.getMaxScore()));
    }

    grade.setScore(gradeDto.getScore());
    grade.setGradedAt(LocalDateTime.now());
    grade.setComments(gradeDto.getComments());
    grade.setGradeStatus(GradeStatus.RATED);

    gradeRepository.save(grade);
  }
}
