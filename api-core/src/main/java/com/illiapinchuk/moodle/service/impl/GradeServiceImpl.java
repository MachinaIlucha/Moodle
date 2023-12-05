package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.exception.NotValidInputException;
import com.illiapinchuk.moodle.exception.SubmissionAlreadyGradedException;
import com.illiapinchuk.moodle.model.dto.GradeDto;
import com.illiapinchuk.moodle.model.entity.GradeStatus;
import com.illiapinchuk.moodle.persistence.entity.Grade;
import com.illiapinchuk.moodle.persistence.repository.GradeRepository;
import com.illiapinchuk.moodle.service.business.GradeService;
import com.illiapinchuk.moodle.service.SubmissionService;
import com.illiapinchuk.moodle.service.TaskService;
import com.illiapinchuk.moodle.service.crud.GradeCRUDService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradeServiceImpl implements GradeService, GradeCRUDService {

  private final SubmissionService submissionService;
  private final TaskService taskService;

  private final GradeRepository gradeRepository;

  @Override
  public void gradeSubmission(
      @Nonnull final String taskId,
      @Nonnull final String submissionId,
      @Nonnull final GradeDto gradeDto) {
    final var task = taskService.getTaskById(taskId);
    final var submission = submissionService.getSubmissionById(submissionId);

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

  @Override
  public Grade saveGrade(@Nonnull final Grade grade) {
    return gradeRepository.save(grade);
  }
}
