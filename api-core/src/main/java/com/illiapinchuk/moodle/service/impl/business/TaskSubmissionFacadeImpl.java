package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.common.mapper.SubmissionMapper;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.TaskValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.TaskNotFoundException;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.SubmissionDto;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Grade;
import com.illiapinchuk.moodle.persistence.entity.Submission;
import com.illiapinchuk.moodle.persistence.entity.TaskSubmissionFile;
import com.illiapinchuk.moodle.service.business.CourseService;
import com.illiapinchuk.moodle.service.business.FileUploadService;
import com.illiapinchuk.moodle.service.business.TaskSubmissionFacade;
import com.illiapinchuk.moodle.service.crud.GradeCRUDService;
import com.illiapinchuk.moodle.service.crud.SubmissionCRUDService;
import com.illiapinchuk.moodle.service.crud.TaskCRUDService;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/** Implementation of {@link TaskSubmissionFacade} interface. */
@Service
@RequiredArgsConstructor
public class TaskSubmissionFacadeImpl implements TaskSubmissionFacade {

  private final TaskCRUDService taskCRUDService;
  private final CourseService courseService;
  private final FileUploadService fileUploadService;
  private final GradeCRUDService gradeCRUDService;
  private final SubmissionCRUDService submissionCRUDService;

  private final SubmissionMapper submissionMapper;
  private final TaskMapper taskMapper;

  private final UserValidator userValidator;
  private final TaskValidator taskValidator;

  @Override
  @Transactional(rollbackOn = Exception.class)
  public TaskDto addSubmissionToTask(
      @Nonnull final String submissionJson,
      @Nonnull final List<MultipartFile> files,
      @Nonnull final String taskId) {
    final var submissionDto = submissionMapper.fromJson(submissionJson);
    validateTaskAndUserExistence(submissionDto);

    final var submission = submissionMapper.submissionDtoToSubmission(submissionDto);
    processSubmittedFiles(files, submissionDto.getTaskId(), submissionDto.getUserId(), submission);

    final var grade = createAndSaveGrade(submissionDto, taskId);
    submission.setGrade(grade);

    final var savedSubmission = submissionCRUDService.saveSubmission(submission);
    updateTaskWithSubmission(taskId, savedSubmission.getId());

    return taskMapper.taskToTaskDto(taskCRUDService.getTaskById(taskId));
  }

  private void validateTaskAndUserExistence(@Nonnull final SubmissionDto submissionDto) {
    if (!taskValidator.isTaskExistsInDbById(submissionDto.getTaskId())) {
      throw new TaskNotFoundException(
          "Task with id: " + submissionDto.getTaskId() + " does not exist.");
    }
    if (!userValidator.isUserExistInDbById(submissionDto.getUserId())) {
      throw new UserNotFoundException(
          "User with id: " + submissionDto.getUserId() + " does not exist.");
    }
  }

  private Grade createAndSaveGrade(
      @Nonnull final SubmissionDto submissionDto, @Nonnull final String taskId) {
    var courseId = courseService.getCourseIdByTaskId(taskId);
    var grade =
        Grade.builder()
            .taskId(taskId)
            .courseId(courseId)
            .studentId(submissionDto.getUserId())
            .build();
    return gradeCRUDService.saveGrade(grade);
  }

  private void updateTaskWithSubmission(
      @Nonnull final String taskId, @Nonnull final String submissionId) {
    var task = taskCRUDService.getTaskById(taskId);
    task.getSubmissionIds().add(submissionId);
    taskCRUDService.updateTask(task);
  }

  /**
   * Process and save submitted files, associating them with a submission.
   *
   * @param files A list of files uploaded with the submission.
   * @param taskId The unique identifier of the task to which the submission belongs.
   * @param userId The unique identifier of the user who submitted the files.
   * @param submissionRequest The Submission entity to which the files are associated.
   */
  private void processSubmittedFiles(
      @NotEmpty final List<MultipartFile> files,
      @Nonnull final String taskId,
      @Nonnull final Long userId,
      @Nonnull final Submission submissionRequest) {
    // Process each uploaded file and create TaskSubmissionFile entities
    final var taskSubmissionFiles =
        files.stream()
            .map(
                file -> {
                  // Upload the file and obtain the file name
                  final var fileName = fileUploadService.uploadFile(file);

                  // Create a TaskSubmissionFile entity for the uploaded file
                  return TaskSubmissionFile.builder()
                      .taskId(taskId)
                      .userId(userId)
                      .fileName(fileName)
                      .build();
                })
            .toList();

    // Associate the created TaskSubmissionFile entities with the submission
    submissionRequest.setSubmissionFiles(taskSubmissionFiles);
  }
}
