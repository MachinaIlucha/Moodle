package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.constants.ApplicationConstants;
import com.illiapinchuk.moodle.common.date.DateService;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.TaskValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.TaskNotFoundException;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.persistence.repository.TaskRepository;
import com.illiapinchuk.moodle.service.TaskService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Implementation of {@link TaskService} interface. */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;
  private final TaskValidator taskValidator;
  private final DateService dateService;
  private final UserValidator userValidator;

  @Override
  public Task getTaskById(@NotNull final String id) {
    return taskRepository
        .findById(id)
        .orElseThrow(
            () -> new TaskNotFoundException(String.format("Task with id: %s not found", id)));
  }

  @Override
  public Task createTask(@NotNull final Task task) {
    final var authorId = task.getAuthorId();
    if (!userValidator.isUserExistInDbById(authorId))
      throw new UserNotFoundException(
          String.format("The author with id: %s does not exist.", authorId));

    task.setCreationDate(
        dateService.getCurrentZonedDateTime(ApplicationConstants.Web.Security.SERVER_TIMEZONE_ID));
    return taskRepository.save(task);
  }

  @Override
  public Task updateTask(@NotNull final TaskDto taskDto) {
    final var taskId = taskDto.getId();
    if (!taskValidator.isTaskExistsInDbById(taskId)) {
      throw new TaskNotFoundException(String.format("Task with id: %s not found", taskId));
    }
    final var task = getTaskById(taskId);
    taskMapper.updateTask(task, taskDto);

    return taskRepository.save(task);
  }

  @Override
  public void deleteTaskById(@NotNull final String id) {
    taskRepository.deleteById(id);
  }

  @Override
  public List<Task> getTasksByCourseId(@NotNull String courseId) {
    return taskRepository.getTasksByCourseId(courseId);
  }
}
