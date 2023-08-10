package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.ApplicationConstants;
import com.illiapinchuk.moodle.common.date.DateService;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.common.validator.TaskValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.TaskNotFoundException;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.persistence.repository.TaskRepository;
import com.illiapinchuk.moodle.service.TaskAttachmentService;
import com.illiapinchuk.moodle.service.TaskService;
import jakarta.annotation.Nonnull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Implementation of {@link TaskService} interface. */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;
  private final TaskValidator taskValidator;
  private final DateService dateService;
  private final UserValidator userValidator;
  private final TaskAttachmentService taskAttachmentService;
  private final CourseValidator courseValidator;

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
    // Retrieve the author ID from the task
    final var authorId = task.getAuthorId();

    // Check if the author exists in the database
    if (!userValidator.isUserExistInDbById(authorId)) {
      throw new UserNotFoundException(
          String.format("The author with id: %s does not exist.", authorId));
    }

    // Set the creation date using the date service
    final var creationDate =
        dateService.getCurrentZonedDateTime(ApplicationConstants.Web.Security.SERVER_TIMEZONE_ID);
    task.setCreationDate(creationDate);

    // Save the task in the repository
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
}
