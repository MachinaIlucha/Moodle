package com.illiapinchuk.moodle.service.impl.crud;

import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.common.validator.TaskValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.TaskNotFoundException;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.persistence.repository.TaskRepository;
import com.illiapinchuk.moodle.service.business.TaskAttachmentService;
import com.illiapinchuk.moodle.service.crud.SubmissionCRUDService;
import com.illiapinchuk.moodle.service.crud.TaskCRUDService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/** Implementation of {@link TaskCRUDService} interface. */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskCRUDServiceImpl implements TaskCRUDService {

  private final TaskRepository taskRepository;

  private final TaskAttachmentService taskAttachmentService;
  private final SubmissionCRUDService submissionCRUDService;

  private final TaskMapper taskMapper;

  private final TaskValidator taskValidator;
  private final CourseValidator courseValidator;

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
                                    submissionCRUDService
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
  public Task updateTaskFromDto(@Nonnull final TaskDto taskDto) {
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
}
