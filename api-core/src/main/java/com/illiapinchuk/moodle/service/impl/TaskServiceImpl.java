package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.mapper.SubmissionMapper;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.common.validator.TaskValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.TaskNotFoundException;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Submission;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import com.illiapinchuk.moodle.persistence.entity.TaskSubmissionFile;
import com.illiapinchuk.moodle.persistence.repository.TaskRepository;
import com.illiapinchuk.moodle.service.FileUploadService;
import com.illiapinchuk.moodle.service.TaskAttachmentService;
import com.illiapinchuk.moodle.service.TaskService;
import jakarta.annotation.Nonnull;
import java.util.List;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Implementation of {@link TaskService} interface. */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;
  private final TaskValidator taskValidator;
  private final TaskAttachmentService taskAttachmentService;

  private final CourseValidator courseValidator;
  private final UserValidator userValidator;

  private final FileUploadService fileUploadService;
  private final SubmissionMapper submissionMapper;

  @Override
  @Transactional
  public TaskDto addAttachmentToTask(
      @Nonnull final MultipartFile file, @Nonnull final String taskId) {
    // Upload the file and get the file name
    final var fileName = fileUploadService.uploadFile(file);

    // Create and save taskAttachment in database
    final var taskAttachment = TaskAttachment.builder().taskId(taskId).fileName(fileName).build();
    taskAttachmentService.saveTaskAttachment(taskAttachment);

    // Update task (add new file to attachments)
    final var task = getTaskById(taskId);
    task.getAttachments().add(taskAttachment);

    final var taskDto = taskMapper.taskToTaskDto(task);
    final var updatedTask = updateTask(taskDto);

    // Get task attachments
    updatedTask.setAttachments(taskAttachmentService.getAttachmentsByTaskId(taskId));

    return taskMapper.taskToTaskDto(updatedTask);
  }

  @Override
  @Transactional
  public TaskDto addSubmissionToTask(
      @Nonnull final String submissionJson,
      @Nonnull final List<MultipartFile> files,
      @Nonnull final String taskId) {
    // Parse submission data from JSON
    final var submissionDto = submissionMapper.fromJson(submissionJson);

    if (!taskValidator.isTaskExistsInDbById(submissionDto.getTaskId())) {
      throw new TaskNotFoundException(
          String.format("Task with id: %s does not exist.", submissionDto.getTaskId()));
    }

    // Map SubmissionDto to Submission entity
    final var submissionRequest = submissionMapper.submissionDtoToSubmission(submissionDto);

    final var task = getTaskById(taskId);
    // Associate the submission with the task
    submissionRequest.setTask(task);

    final var userId = submissionDto.getUserId();
    if (!userValidator.isUserExistInDbById(userId)) {
      throw new UserNotFoundException(String.format("User with id: %s does not exist.", userId));
    }

    // Process and save uploaded files related to the submission
    processSubmittedFiles(files, taskId, userId, submissionRequest);

    // Add the submission to the task
    task.getSubmissions().add(submissionRequest);

    final var updatedTask = updateTask(taskMapper.taskToTaskDto(task));

    // Return the updated task as a TaskDto
    return taskMapper.taskToTaskDto(updatedTask);
  }

  @Override
  public Task getTaskById(@Nonnull final String taskId) {
    var userId = UserPermissionService.getJwtUser().getId();

    if (!courseValidator.isStudentEnrolledInCourseWithTask(taskId, userId)
        && !UserPermissionService.hasAnyRulingRole()) {
      log.error("User with id - {} trying to access task with id - {}", userId, taskId);
      throw new UserDontHaveAccessToResource("User doesn't have access to this task.");
    }

    return taskRepository
        .findById(taskId)
        .map(
            task -> {
              task.setAttachments(taskAttachmentService.getAttachmentsByTaskId(taskId));
              return task;
            })
        .orElseThrow(
            () -> new TaskNotFoundException(String.format("Task with id: %s not found", taskId)));
  }

  @Override
  public Task createTask(@Nonnull final Task task) {
    return taskRepository.save(task);
  }

  @Override
  public Task updateTask(@Nonnull final TaskDto taskDto) {
    final var taskId = taskDto.getId();
    if (!taskValidator.isTaskExistsInDbById(taskId)) {
      throw new TaskNotFoundException(String.format("Task with id: %s not found", taskId));
    }
    final var task = getTaskById(taskId);
    taskMapper.updateTask(task, taskDto);

    return taskRepository.save(task);
  }

  @Override
  public void deleteTaskById(@Nonnull final String id) {
    final var taskAttachments = taskAttachmentService.getAttachmentsByTaskId(id);

    // Delete task attachments
    taskAttachments.forEach(taskAttachmentService::deleteTaskAttachment);

    // Delete the task
    taskRepository.deleteById(id);
  }

  @Override
  public List<Task> getTasksByCourseId(@Nonnull final String courseId) {
    return taskRepository.getTasksByCourseId(courseId);
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
