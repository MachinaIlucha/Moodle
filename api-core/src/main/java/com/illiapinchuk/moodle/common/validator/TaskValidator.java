package com.illiapinchuk.moodle.common.validator;

import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.persistence.repository.TaskRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Validation for task-related information. */
@Component
@RequiredArgsConstructor
public class TaskValidator {

  private final TaskRepository taskRepository;

  /**
   * Check if the given task exists in the database.
   *
   * @param id the id to check
   * @return true if the task with this id exists in the database, false otherwise
   */
  public boolean isTaskExistsInDbById(@Nonnull final String id) {
    return taskRepository.existsById(id);
  }

  /**
   * Check if the teacher can modify the task.
   *
   * @param taskId the id of the task to check
   * @return true if the teacher can modify the task, false otherwise
   */
  public boolean isTeacherCanModifyTask(@Nonnull final String taskId) {
    return taskRepository
        .findById(taskId)
        .map(
            task ->
                task.getCourse()
                    .getAuthorIds()
                    .contains(UserPermissionService.getJwtUser().getId()))
        .orElse(false);
  }

  /**
   * Check if the student has access to the task.
   *
   * @param taskId the id of the task to check
   * @return true if the student has access to the task, false otherwise
   */
  public boolean isStudentHaveAccessToTask(@Nonnull final String taskId) {
    return taskRepository
        .findById(taskId)
        .map(
            task ->
                task.getCourse().getStudents().contains(UserPermissionService.getJwtUser().getId()))
        .orElse(false);
  }
}
