package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.common.validator.TaskValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.TaskNotFoundException;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import com.illiapinchuk.moodle.persistence.repository.TaskRepository;
import com.illiapinchuk.moodle.service.FileUploadService;
import com.illiapinchuk.moodle.service.SubmissionService;
import com.illiapinchuk.moodle.service.TaskAttachmentService;
import com.illiapinchuk.moodle.service.TaskService;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
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
  private final SubmissionService submissionService;

  private final CourseValidator courseValidator;

  private final FileUploadService fileUploadService;

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
  public Task getTaskById(@Nonnull final String taskId) {
    final var userId = UserPermissionService.getJwtUser().getId();

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
              if (!UserPermissionService.hasAnyRulingRole()) {
                task.setSubmissionIds(
                    task.getSubmissionIds().stream()
                        .filter(
                            submissionId ->
                                submissionService
                                    .getSubmissionById(submissionId)
                                    .getUserId()
                                    .equals(userId))
                        .collect(Collectors.toList()));
              }
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
  public Task updateTask(@Nonnull final Task task) {
    final var taskId = task.getId();
    if (!taskValidator.isTaskExistsInDbById(taskId)) {
      throw new TaskNotFoundException(String.format("Task with id: %s not found", taskId));
    }

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
}
